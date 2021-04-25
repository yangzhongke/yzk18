package com.yzk18.GUI;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import com.github.lgooddatepicker.components.*;
import com.github.lgooddatepicker.zinternaltools.JIntegerTextField;
import com.yzk18.commons.CommonHelpers;
import com.yzk18.commons.ReflectionHelpers;

/**
 * <div lang="zh-cn">各种简化的对话框等GUI相关的工具类。</div>
 */
public class GUI {

    static
    {
        if(Locale.SIMPLIFIED_CHINESE.equals(Locale.getDefault()))
        {
            //https://stackoverflow.com/questions/14407804/how-to-change-the-default-text-of-buttons-in-joptionpane-showinputdialog/14408288
            UIManager.put("OptionPane.userNameText", "用户名");
            UIManager.put("OptionPane.passwordText", "密码");
            UIManager.put("JFileChoose.saveExistConfirmText","选中内容已经存在，确认覆盖吗？");
        }
        else
        {
            UIManager.put("OptionPane.userNameText", "UserName");
            UIManager.put("OptionPane.passwordText", "Password");
            UIManager.put("JFileChoose.saveExistConfirmText","This item exists, overwrite?");
        }
    }

    //the owner of other dialog, which can help to show an item in the taskbar
    private static volatile DummyFrame dummyFrame = null;
    private static volatile Thread mainThread;

    private static void showDummyFrameIfNeeded()
    {
        if(dummyFrame!=null)
        {
            return;
        }
        dummyFrame = new DummyFrame();
        mainThread = Thread.currentThread();
        mainThread.setUncaughtExceptionHandler((t,e)->{
            //closeDummyFrame() when unhandled exception is thrown in main thread
            e.printStackTrace();
            GUI.closeDummyFrame();
        });
        Thread t = new Thread(()->{
            while(mainThread.isAlive())
            {
                try
                {
                    Thread.sleep(100);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            //closeDummyFrame() when main() exits
            GUI.closeDummyFrame();
        });
        t.setDaemon(false);
        t.start();
    }

    private static void closeDummyFrame()
    {
        if(dummyFrame!=null)
        {
            dummyFrame.dispose();
            dummyFrame = null;
        }
    }

    /**
     * <div lang="zh-cn">获取用户显示TaskBar中对应内容的隐藏窗口</div>
     * @return
     */
    public static DummyFrame getDummyFrame()
    {
        showDummyFrameIfNeeded();
        return dummyFrame;
    }

    /**
     * <div lang="zh-cn">弹出普通消息对话框。</div>
     * @param message <div lang="zh-cn">消息</div>
     */
    public static void msgBox(Object message)
    {
        showDummyFrameIfNeeded();
        JOptionPane.showMessageDialog(dummyFrame,CommonHelpers.toString(message),null,JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * <div lang="zh-cn">弹出报错消息对话框。</div>
     * @param message <div lang="zh-cn">消息</div>
     */
    public static void errorBox(Object message)
    {
        showDummyFrameIfNeeded();
        JOptionPane.showMessageDialog(dummyFrame,CommonHelpers.toString(message),null,JOptionPane.ERROR_MESSAGE);
    }

    /**
     * <div lang="zh-cn">弹出包含【是】、【否】两个按钮的消息对话框</div>
     * @param message <div lang="zh-cn">消息</div>
     * @return <div lang="zh-cn">如果点击了【是】则返回true，否则返回false</div>
     */
    public static boolean yesNoBox(Object message)
    {
        int ret = JOptionPane.showConfirmDialog(dummyFrame,CommonHelpers.toString(message),null,JOptionPane.YES_NO_OPTION);
        return ret==JOptionPane.YES_OPTION;
    }

    /**
     * <div lang="zh-cn">弹出包含【确认】、【取消】两个按钮的消息对话框</div>
     * @param message <div lang="zh-cn">消息</div>
     * @return <div lang="zh-cn">如果点击了【确认】则返回true，否则返回false</div>
     */
    public static boolean okCancelBox(Object message)
    {
        showDummyFrameIfNeeded();
        int ret = JOptionPane.showConfirmDialog(dummyFrame,CommonHelpers.toString(message),null,JOptionPane.OK_CANCEL_OPTION);
        return ret==JOptionPane.OK_OPTION;
    }

    /**
     * <div lang="zh-cn">弹出允许用户输入一段字符串的对话框</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @param initialValue <div lang="zh-cn">初始值</div>
     * @return <div lang="zh-cn">用户输入的字符串，如果点击了取消，则返回null</div>
     */
    public static String inputBox(Object message,Object initialValue)
    {
        showDummyFrameIfNeeded();
        Object ret = JOptionPane.showInputDialog(dummyFrame,CommonHelpers.toString(message),null,JOptionPane.PLAIN_MESSAGE,
                null,null,initialValue);
        if(ret==null)
        {
            return "";
        }
        else
        {
            return ret.toString();
        }
    }

    /**
     * <div lang="zh-cn">弹出允许用户输入一段字符串的对话框</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @return <div lang="zh-cn">用户输入的字符串，如果点击了取消，则返回null</div>
     */
    public static String inputBox(Object message)
    {
        return inputBox(message,"");
    }

    /**
     * <div lang="zh-cn">弹出允许用户输入一个整数的对话框</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @param initialValue <div lang="zh-cn">初始值</div>
     * @return <div lang="zh-cn">用户输入的值，如果点击了取消，则返回null</div>
     */
    public static Integer intBox(Object message,Integer initialValue)
    {
        showDummyFrameIfNeeded();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(CommonHelpers.toString(message));
        panel.add(label);
        JIntegerTextField intField = new JIntegerTextField();
        if(initialValue!=null)
        {
            intField.setValue(initialValue);
        }
        DefaultFocusAncestorListener.setDefaultFocusedComponent(panel,intField);
        panel.add(intField);

        int result = JOptionPane.showOptionDialog(dummyFrame, panel, null,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, null, null);
        if(result!=JOptionPane.OK_OPTION)
        {
            return null;
        }
        return intField.getValue();
    }

    /**
     * <div lang="zh-cn">弹出允许用户输入一个整数的对话框</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @return <div lang="zh-cn">用户输入的值，如果点击了取消，则返回null</div>
     */
    public static Integer intBox(Object message)
    {
        return intBox(message,null);
    }

    /**
     * <div lang="zh-cn">弹出允许用户输入一个double值的对话框</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @param initialValue <div lang="zh-cn">用户输入的值，如果点击了取消，则返回null</div>
     * @return <div lang="zh-cn"></div>
     */
    public static Double doubleBox(Object message,Double initialValue)
    {
        showDummyFrameIfNeeded();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(CommonHelpers.toString(message));
        panel.add(label);
        JDoubleTextField doubleField = new JDoubleTextField();
        if(initialValue!=null)
        {
            doubleField.setValue(initialValue);
        }
        DefaultFocusAncestorListener.setDefaultFocusedComponent(panel,doubleField);
        panel.add(doubleField);

        int result = JOptionPane.showOptionDialog(dummyFrame, panel, null,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, null, null);
        if(result!=JOptionPane.OK_OPTION)
        {
            return null;
        }
        return doubleField.getValue();
    }

    /**
     * <div lang="zh-cn">弹出允许用户输入一个double值的对话框</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @return <div lang="zh-cn">用户输入的值，如果点击了取消，则返回null</div>
     */
    public static Double doubleBox(Object message)
    {
        return doubleBox(message,null);
    }

    /**
     * <div lang="zh-cn">弹出一个有多个可选值的对话框</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @param initialValue <div lang="zh-cn">默认选中的值</div>
     * @param selectionItems <div lang="zh-cn">可选值</div>
     * @return <div lang="zh-cn">用户选择的值，如果没有选择值则返回null</div>
     */
    public static String choiceBox(Object message, String initialValue, Collection<String> selectionItems)
    {
        return choiceBox(message,initialValue,selectionItems.toArray(new String[selectionItems.size()]));
    }

    /**
     * <div lang="zh-cn">弹出一个有多个可选值的对话框</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @param initialValue <div lang="zh-cn">默认选中的值</div>
     * @param selectionItems <div lang="zh-cn">可选值</div>
     * @return <div lang="zh-cn">用户选择的值，如果没有选择值则返回null</div>
     */
    public static String choiceBox(Object message, String initialValue, String... selectionItems)
    {
        showDummyFrameIfNeeded();
        Object ret = JOptionPane.showInputDialog(dummyFrame,CommonHelpers.toString(message),null,JOptionPane.PLAIN_MESSAGE,
                null,selectionItems,initialValue);
        return (String)ret;
    }

    /**
     * <div lang="zh-cn">弹出一个有多个可选值的对话框</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @param selectionItems <div lang="zh-cn">可选值</div>
     * @return <div lang="zh-cn">用户选择的值，如果没有选择值则返回null</div>
     */
    public static String choiceBox(Object message, Collection<String> selectionItems)
    {
        return choiceBox(message,selectionItems.toArray(new String[selectionItems.size()]));
    }

    /**
     * <div lang="zh-cn">弹出一个有多个可选值的对话框</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @param selectionItems <div lang="zh-cn">可选值</div>
     * @return <div lang="zh-cn">用户选择的值，如果没有选择值则返回null</div>
     */
    public static String choiceBox(Object message, String... selectionItems)
    {
        return choiceBox(message,"",selectionItems);
    }

    /**
     * <div lang="zh-cn">弹出有多个输入内容的对话框</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @param labels <div lang="zh-cn">分别是各个输入内容项的显示的提示文本</div>
     * @param initialValues <div lang="zh-cn">每个控件的初始值，可空或者数量可以与labels的数量不一致</div>
     * @param types <div lang="zh-cn">每个输入域的数据类型，对话框会根据数据类型生成合适的控件。可空或者数量可以与labels的数量不一致。对于File、int、double、LocalDate、
     *      *      * LocalDateTime、LocalTime、boolean等支持个性化编辑器，其他都用普通文本编辑器。</div>
     * @return <div lang="zh-cn">各个输入内容的String类型的值，数量和labels的数量一致；如果用户点击了【取消】按钮，则返回null</div>
     */
    public static String[] multiInputBox(Object message,Collection<String> labels,
                                         Collection<Object> initialValues,Collection<Class> types)
    {
        String[] initValuesArray = null;
        if(initialValues!=null)
        {
            initValuesArray = initialValues.toArray(new String[initialValues.size()]);
        }
        Class[] typesArray = null;
        if(types!=null)
        {
            typesArray = types.toArray(new Class[types.size()]);
        }
        return multiInputBox(message,labels.toArray(new String[labels.size()]),
                initValuesArray,typesArray);
    }

    /**
     * <div lang="zh-cn">弹出有多个输入内容的对话框</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @param labels <div lang="zh-cn">分别是各个输入内容项的显示的提示文本</div>
     * @return <div lang="zh-cn">各个输入内容的String类型的值，数量和labels的数量一致；如果用户点击了【取消】按钮，则返回null</div>
     */
    public static String[] multiInputBox(Object message,String... labels)
    {
        return multiInputBox(message,labels,null,null);
    }

    private static JComponent createEditor(Class type,Object initValue)
    {
        if(type==Integer.class||type==int.class||type==Short.class||type==short.class
                ||type==Byte.class||type==byte.class)
        {
            JIntegerTextField txtField = new JIntegerTextField();
            if(initValue!=null)
            {
                txtField.setValue(CommonHelpers.toInt(initValue));
            }
            return txtField;
        }
        else if(type==Double.class||type==double.class||type==Float.class||type==float.class)
        {
            JDoubleTextField txtField = new JDoubleTextField();
            if(initValue!=null)
            {
                txtField.setValue(CommonHelpers.toInt(initValue));
            }
            return txtField;
        }
        else if(type==Boolean.class||type==boolean.class)
        {
            JCheckBox checkBox = new JCheckBox();
            if(initValue!=null)
            {
                checkBox.setSelected(CommonHelpers.toBoolean(initValue));
            }
            return checkBox;
        }
        else if(type==LocalDate.class)
        {
            DatePicker picker = new DatePicker();
            if(initValue!=null)
            {
                picker.setDate(CommonHelpers.toLocalDate(initValue));
            }
            return picker;
        }
        else if(type==LocalTime.class)
        {
            TimePickerSettings timePickerSettings = DateTimePickersUtils.createTimePickerSettings();
            TimePicker picker = new TimePicker(timePickerSettings);
            if(initValue!=null)
            {
                picker.setTime(CommonHelpers.toLocalTime(initValue));
            }
            return picker;
        }
        else if(type==LocalDateTime.class)
        {
            TimePickerSettings timePickerSettings = DateTimePickersUtils.createTimePickerSettings();
            DateTimePicker picker = new DateTimePicker(new DatePickerSettings(),timePickerSettings);
            if(initValue!=null)
            {
                picker.setDateTimeStrict(CommonHelpers.toLocalDateTime(initValue));
            }
            return picker;
        }
        else if(type==File.class)
        {
            FilePicker picker = new FilePicker();
            if(initValue!=null)
            {
                String txt = CommonHelpers.toString(initValue);
                picker.setFile(new File(txt));
            }
            return picker;
        }
        else
        {
            JTextField txtField = new JTextField(CommonHelpers.toString(initValue));
            return txtField;
        }
    }

    private static Object getEditorValue(JComponent componentEditor)
    {
        if(componentEditor instanceof JTextField)
        {
            JTextField txtField = (JTextField)componentEditor;
            return txtField.getText();
        }
        else if(componentEditor instanceof  DatePicker)
        {
            DatePicker picker = (DatePicker)componentEditor;
            return picker.getDate();
        }
        else if(componentEditor instanceof  JCheckBox)
        {
            JCheckBox checkBox = (JCheckBox)componentEditor;
            return checkBox.isSelected();
        }
        else if(componentEditor instanceof  TimePicker)
        {
            TimePicker picker = (TimePicker)componentEditor;
            return picker.getTime();
        }
        else if(componentEditor instanceof  DateTimePicker)
        {
            DateTimePicker picker = (DateTimePicker)componentEditor;
            return picker.getDateTimeStrict();
        }
        else if(componentEditor instanceof  FilePicker)
        {
            FilePicker picker = (FilePicker)componentEditor;
            return picker.getFile();
        }
        else
        {
            throw new IllegalArgumentException("unknow control type:"+componentEditor.getClass());
        }
    }

    /**
     * <div lang="zh-cn">弹出有多个输入内容的对话框</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @param labels <div lang="zh-cn">分别是各个输入内容项的显示的提示文本</div>
     * @param initialValues <div lang="zh-cn">每个控件的初始值，可空或者数量可以与labels的数量不一致</div>
     * @param types <div lang="zh-cn">每个输入域的数据类型，对话框会根据数据类型生成合适的控件。可空或者数量可以与labels的数量不一致。 对于File、int、double、LocalDate、
     *      * LocalDateTime、LocalTime、boolean等支持个性化编辑器，其他都用普通文本编辑器。</div>
     * @return <div lang="zh-cn">各个输入内容的String类型的值，数量和labels的数量一致；如果用户点击了【取消】按钮，则返回null</div>
     */
    public static String[] multiInputBox(Object message,String[] labels,Object[] initialValues,Class[] types)
    {
        showDummyFrameIfNeeded();
        if(initialValues!=null&&labels.length<initialValues.length)
        {
            throw new IllegalArgumentException("labels.length<initialValues.length");
        }
        Dimension halfSize = getDefaultDialogSize();

        int fieldsCount = labels.length;

        JPanel rootPanel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        rootPanel.setLayout(layout);

        JLabel labelMsg = new JLabel(CommonHelpers.toString(message));
        rootPanel.add(labelMsg);

        GridBagConstraints gbcLabelMsg = new GridBagConstraints();
        gbcLabelMsg.gridx=0;
        gbcLabelMsg.gridy=0;
        gbcLabelMsg.gridwidth=2;
        layout.setConstraints(labelMsg,gbcLabelMsg);

        ArrayList<JComponent> editors = new ArrayList<>(fieldsCount);
        for(int i=0;i<fieldsCount;i++)
        {
            String labelText = labels[i];
            Object initValue="";
            if(initialValues!=null&&i<initialValues.length)
            {
                initValue = initialValues[i];
            }
            JLabel label = new JLabel(labelText);
            rootPanel.add(label);

            JComponent componentEditor;
            if(types!=null&&i<types.length)
            {
                Class type = types[i];
                componentEditor = createEditor(type,initValue);
            }
            else
            {
                JTextField txtField = new JTextField(CommonHelpers.toString(initValue));
                componentEditor = txtField;
            }

            if(i==0)
            {
                DefaultFocusAncestorListener.setDefaultFocusedComponent(rootPanel,componentEditor);
            }
            rootPanel.add(componentEditor);
            editors.add(componentEditor);

            GridBagConstraints gbcLabel = new GridBagConstraints();
            gbcLabel.gridx=0;
            gbcLabel.gridy=i+1;
            gbcLabel.gridwidth=1;
            layout.setConstraints(label,gbcLabel);

            GridBagConstraints gbcTxt = new GridBagConstraints();
            gbcTxt.gridx=1;
            gbcTxt.gridy=i+1;
            gbcTxt.weightx=1;
            gbcTxt.gridwidth=1;
            gbcTxt.fill = GridBagConstraints.HORIZONTAL;
            layout.setConstraints(componentEditor,gbcTxt);
        }

        JScrollPane scrollPane = new JScrollPane(rootPanel);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(halfSize);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        int result = JOptionPane.showConfirmDialog(dummyFrame, scrollPane,
                null, JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
        if(result!=JOptionPane.OK_OPTION)
        {
            return null;
        }

        String[] results = new String[editors.size()];
        for(int i=0;i<results.length;i++)
        {
            JComponent componentEditor = editors.get(i);
            Object value = getEditorValue(componentEditor);
            results[i] = CommonHelpers.toString(value);
        }

        return results;
    }

    /**
     * <div lang="zh-cn">弹出有多个输入内容的对话框</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @param data <div lang="zh-cn">多个项的设置，key是显示的名字，值是初始值（可以为null），如果值不是null，则用初始值的类型来设定编辑器。对于File、int、double、LocalDate、
     *      * LocalDateTime、LocalTime、boolean等支持个性化编辑器，其他都用普通文本编辑器。</div>
     * @return <div lang="zh-cn"></div>
     */
    public static Map<String,String> multiInputBox(Object message,Map<String,Object> data)
    {
        String[] labels = new String[data.size()];
        Object[] initialValues =new Object[data.size()];
        Class[] types = new Class[data.size()];
        int counter=0;
        for(Map.Entry<String,Object> entry : data.entrySet())
        {
            String name = entry.getKey();
            Object value = entry.getValue();
            labels[counter] = name;
            initialValues[counter] = value;
            types[counter] = (value==null?String.class:value.getClass());
            counter++;
        }
        String[] returnedValues = multiInputBox(message,labels,initialValues,types);
        if(returnedValues==null)
        {
            return null;
        }
        Map<String,String> returnedMap = new LinkedHashMap<>();
        for(int i=0;i<labels.length;i++)
        {
            returnedMap.put(labels[i],returnedValues[i]);
        }
        return returnedMap;
    }

    /**
     * <div lang="zh-cn">弹出用data对象构造含有多输入项内容的对话框。</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @param data <div lang="zh-cn">根据这个对象来构建多个输入项内容，遵从JavaBean规范，同时支持chain setter方法。
     * 属性的名字为提示消息，属性的值为初始值，根据属性的类型来构建编辑器。对于File、int、double、LocalDate、
     * LocalDateTime、LocalTime、boolean等支持个性化编辑器，其他都用普通文本编辑器。</div>
     * @param <T> <div lang="zh-cn"></div>
     * @return <div lang="zh-cn"></div>
     */
    public static <T> T objectInputBox(Object message,T data)
    {
        if(data==null)
        {
            throw new IllegalArgumentException("data==null");
        }
        //BeanUtils of commons-lang3 not works for chain setter,
        //so use Introspector instead
        //https://stackoverflow.com/questions/22743765/beanutils-not-works-for-chain-setter
        Class objType = data.getClass();
        try {
            PropertyDescriptor[] props = ReflectionHelpers.getRWPropertyDescriptors(objType);
            String[] labels = new String[props.length];
            Object[] initValues = new Object[props.length];
            Class[] types = new Class[props.length];
            for(int i=0;i<props.length;i++)
            {
                PropertyDescriptor prop = props[i];
                String propName = prop.getName();
                Object propValue = ReflectionHelpers.getPropertyValue(data,prop);
                Class dataType = prop.getPropertyType();
                labels[i] = propName;
                initValues[i] = propValue;
                types[i] = dataType;
            }
            String[] results = multiInputBox(message,labels,initValues,types);
            if(results==null)
            {
                return null;
            }
            Object resultObj = objType.getConstructor().newInstance();
            for(int i=0;i<props.length;i++)
            {
                PropertyDescriptor prop = props[i];
                String propValue = results[i];
                Class dataType = prop.getPropertyType();
                Object convertedValue = CommonHelpers.convert(propValue,dataType);
                ReflectionHelpers.setPropertyValue(resultObj,prop,convertedValue);
            }
            return (T)resultObj;
        } catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException
                |NoSuchMethodException|InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <div lang="zh-cn">弹出日期选择对话框。</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @return <div lang="zh-cn">选择的值，如果点击了【取消】按钮，则返回null</div>
     */
    public static LocalDate dateBox(Object message)
    {
        return dateBox(message,null);
    }

    /**
     * <div lang="zh-cn">弹出日期选择对话框。</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @param initialValue <div lang="zh-cn">初始值</div>
     * @return <div lang="zh-cn">选择的值，如果点击了【取消】按钮，则返回null</div>
     */
    public static LocalDate dateBox(Object message, LocalDate initialValue)
    {
        showDummyFrameIfNeeded();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(CommonHelpers.toString(message));
        panel.add(label);

        DatePicker datePicker = new DatePicker();
        if(initialValue!=null)
        {
            datePicker.setDate(initialValue);
        }
        panel.add(datePicker);

        int result = JOptionPane.showOptionDialog(dummyFrame, panel, null,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, null, null);
        if(result!=JOptionPane.OK_OPTION)
        {
            return null;
        }
        return datePicker.getDate();
    }

    /**
     * <div lang="zh-cn">弹出日期时间选择对话框</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @return <div lang="zh-cn">选择的值，如果点击了【取消】按钮，则返回null</div>
     */
    public static LocalDateTime datetimeBox(Object message)
    {
        return datetimeBox(message,null);
    }

    /**
     * <div lang="zh-cn">弹出日期时间选择对话框。</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @param initialValue <div lang="zh-cn">初始值</div>
     * @return <div lang="zh-cn">选择的值，如果点击了【取消】按钮，则返回null</div>
     */
    public static LocalDateTime datetimeBox(Object message, LocalDateTime initialValue)
    {
        showDummyFrameIfNeeded();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(CommonHelpers.toString(message));
        panel.add(label);

        TimePickerSettings timePickerSettings = DateTimePickersUtils.createTimePickerSettings();
        DateTimePicker picker = new DateTimePicker(new DatePickerSettings(),timePickerSettings);
        if(initialValue!=null)
        {
            picker.setDateTimeStrict(initialValue);
        }
        panel.add(picker);

        int result = JOptionPane.showOptionDialog(dummyFrame, panel, null,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, null, null);
        if(result!=JOptionPane.OK_OPTION)
        {
            return null;
        }
        return picker.getDateTimeStrict();
    }

    /**
     * <div lang="zh-cn">弹出日期选择对话框。</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @return <div lang="zh-cn">选择的值，如果点击了【取消】按钮，则返回null</div>
     */
    public static LocalTime timeBox(Object message)
    {
        return timeBox(message,null);
    }

    /**
     * <div lang="zh-cn">弹出日期选择对话框。</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @param initialValue <div lang="zh-cn">初始值</div>
     * @return <div lang="zh-cn">选择的值，如果点击了【取消】按钮，则返回null</div>
     */
    public static LocalTime timeBox(Object message, LocalTime initialValue)
    {
        showDummyFrameIfNeeded();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(CommonHelpers.toString(message));
        panel.add(label);

        TimePickerSettings timePickerSettings = DateTimePickersUtils.createTimePickerSettings();
        TimePicker picker = new TimePicker(timePickerSettings);
        if(initialValue!=null)
        {
            picker.setTime(initialValue);
        }
        panel.add(picker);

        int result = JOptionPane.showOptionDialog(dummyFrame, panel, null,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, null, null);
        if(result!=JOptionPane.OK_OPTION)
        {
            return null;
        }
        return picker.getTime();
    }

    /**
     * <div lang="zh-cn">弹出密码对话框</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @return <div lang="zh-cn">输入的值，如果点击了【取消】按钮，则返回null</div>
     */
    public static String passwordBox(Object message)
    {
        showDummyFrameIfNeeded();
        JPanel panel = new JPanel();
        JLabel label = new JLabel(CommonHelpers.toString(message));
        JPasswordField pwdField = new JPasswordField(10);
        panel.add(label);
        panel.add(pwdField);

        DefaultFocusAncestorListener.setDefaultFocusedComponent(panel,pwdField);

        int result = JOptionPane.showOptionDialog(dummyFrame, panel, null,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, null, null);
        if(result!=JOptionPane.OK_OPTION)
        {
            return null;
        }
        String pwd = new String(pwdField.getPassword());
        return pwd;
    }

    /**
     * <div lang="zh-cn">弹出登录对话框</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @return <div lang="zh-cn">如果点击了【取消】按钮，则返回null；否则返回长度为2的数组，第一个元素为用户输入的用户名，第二个元素为用户输入的密码。</div>
     */
    public static String[] loginBox(Object message)
    {
        showDummyFrameIfNeeded();
        JPanel rootPanel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        rootPanel.setLayout(layout);

        JLabel labelMsg = new JLabel(CommonHelpers.toString(message));
        rootPanel.add(labelMsg);

        GridBagConstraints gbcLabelMsg = new GridBagConstraints();
        gbcLabelMsg.gridx=0;
        gbcLabelMsg.gridy=0;
        gbcLabelMsg.gridwidth=2;
        layout.setConstraints(labelMsg,gbcLabelMsg);

        JLabel labelUserName = new JLabel(UIManager.getString("OptionPane.userNameText"));
        rootPanel.add(labelUserName);
        JTextField tfUserName = new JTextField("");
        rootPanel.add(tfUserName);
        GridBagConstraints gbcUserNameLabel = new GridBagConstraints();
        gbcUserNameLabel.gridx=0;
        gbcUserNameLabel.gridy=1;
        gbcUserNameLabel.gridwidth=1;
        layout.setConstraints(labelUserName,gbcUserNameLabel);

        GridBagConstraints gbcUserNameTxt = new GridBagConstraints();
        gbcUserNameTxt.gridx=1;
        gbcUserNameTxt.gridy=1;
        gbcUserNameTxt.weightx=1;
        gbcUserNameTxt.gridwidth=1;
        gbcUserNameTxt.fill = GridBagConstraints.HORIZONTAL;
        layout.setConstraints(tfUserName,gbcUserNameTxt);

        JLabel labelPassword = new JLabel(UIManager.getString("OptionPane.passwordText"));
        rootPanel.add(labelPassword);
        JPasswordField pfPassword = new JPasswordField();
        rootPanel.add(pfPassword);

        GridBagConstraints gbcPwdLabel = new GridBagConstraints();
        gbcPwdLabel.gridx=0;
        gbcPwdLabel.gridy=2;
        gbcPwdLabel.gridwidth=1;
        layout.setConstraints(labelPassword,gbcPwdLabel);

        GridBagConstraints gbcPwdField = new GridBagConstraints();
        gbcPwdField.gridx=1;
        gbcPwdField.gridy=2;
        gbcPwdField.weightx=1;
        gbcPwdField.gridwidth=1;
        gbcPwdField.fill = GridBagConstraints.HORIZONTAL;
        layout.setConstraints(pfPassword,gbcPwdField);

        DefaultFocusAncestorListener.setDefaultFocusedComponent(rootPanel,tfUserName);

        int result = JOptionPane.showConfirmDialog(dummyFrame, rootPanel,
                null, JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
        if(result!=JOptionPane.OK_OPTION)
        {
            return null;
        }
        String userName = tfUserName.getText();
        String password = new String(pfPassword.getPassword());
        return new String[]{userName,password};
    }

    /**
     * <div lang="zh-cn">弹出包含多个按钮的对话框</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @param buttons <div lang="zh-cn">多个按钮的显示文本</div>
     * @return <div lang="zh-cn">如果点击右上角的【X】关闭了对话框，则返回null；否则返回被点击的按钮的文本。</div>
     */
    public  static String buttonsBox(Object message,Collection<String> buttons)
    {
        return buttonsBox(message,buttons.toArray(new String[buttons.size()]));
    }

    /**
     * <div lang="zh-cn">弹出包含多个按钮的对话框</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @param buttons <div lang="zh-cn">多个按钮的显示文本</div>
     * @return <div lang="zh-cn">如果点击右上角的【X】关闭了对话框，则返回null；否则返回被点击的按钮的文本。</div>
     */
    public  static String buttonsBox(Object message,String... buttons)
    {
        showDummyFrameIfNeeded();
        int result = JOptionPane.showOptionDialog(dummyFrame, CommonHelpers.toString(message), null,
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, buttons, null);
        if(result<0)
        {
            return null;
        }
        else
        {
            return buttons[result];
        }
    }

    /**
     * <div lang="zh-cn">弹出显示图片的对话框</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @param imgPath <div lang="zh-cn">图片路径</div>
     * @param buttons <div lang="zh-cn">多个按钮的显示文本</div>
     * @return <div lang="zh-cn">如果点击右上角的【X】关闭了对话框，则返回null；否则返回被点击的按钮的文本。</div>
     */
    public static String imgBox(Object message,String imgPath,String... buttons)
    {
        ImageIcon imgIcon = new ImageIcon(imgPath);
        return imgBox(message,imgIcon,buttons);
    }

    /**
     * <div lang="zh-cn">弹出显示图片的对话框</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @param image <div lang="zh-cn">图片对象</div>
     * @param buttons <div lang="zh-cn">多个按钮的显示文本</div>
     * @return <div lang="zh-cn">如果点击右上角的【X】关闭了对话框，则返回null；否则返回被点击的按钮的文本。</div>
     */
    public static String imgBox(Object message,Image image,String... buttons)
    {
        return imgBox(message,new ImageIcon(image),buttons);
    }

    /**
     *  <div lang="zh-cn">弹出显示图片的对话框</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @param imageBytes <div lang="zh-cn">图片内容的字节数组</div>
     * @param buttons <div lang="zh-cn">多个按钮的显示文本</div>
     * @return <div lang="zh-cn">如果点击右上角的【X】关闭了对话框，则返回null；否则返回被点击的按钮的文本。</div>
     */
    public static String imgBox(Object message,byte[] imageBytes,String... buttons)
    {
        try(InputStream inStream = new ByteArrayInputStream(imageBytes))
        {
            BufferedImage bufferedImage = ImageIO.read(inStream);
            return imgBox(message,new ImageIcon(bufferedImage),buttons);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     *  <div lang="zh-cn">弹出显示图片的对话框</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @param imageIcon <div lang="zh-cn">图片</div>
     * @param buttons <div lang="zh-cn">多个按钮的显示文本</div>
     * @return <div lang="zh-cn">如果点击右上角的【X】关闭了对话框，则返回null；否则返回被点击的按钮的文本。</div>
     */
    public static String imgBox(Object message,ImageIcon imageIcon,String... buttons)
    {
        showDummyFrameIfNeeded();

        Dimension halfSize = getDefaultDialogSize();

        JPanel panel = new JPanel();
        panel.setPreferredSize(halfSize);
        JLabel labelMsg = new JLabel(CommonHelpers.toString(message));
        panel.add(labelMsg);

        JLabel labelImg = new JLabel(imageIcon);
        JScrollPane scrollPane = new JScrollPane(labelImg);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane);

        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);

        int result = JOptionPane.showOptionDialog(dummyFrame, panel, null,
                JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, buttons, null);
        if(result<0)
        {
            return null;
        }
        else
        {
            return buttons[result];
        }
    }

    private static Dimension getDefaultDialogSize() {
        Dimension screenSize = getScreenSize();
        return new Dimension(screenSize.width / 2, screenSize.height / 2);
    }

    private static  void setFileFilter(JFileChooser fileChooser,String... extensions)
    {
        for(String ext : extensions)
        {
            if(ext==null||ext.trim().length()<=0)
            {
                throw new IllegalArgumentException("extension cannot be null or empty.");
            }
            if(ext.contains("*")||ext.startsWith("."))
            {
                throw new IllegalArgumentException("extension cannot start with . or contain *");
            }
        }
        if(!CommonHelpers.isEmpty(extensions))
        {
            String desc = String.join("|",
                    Arrays.stream(extensions).map(f->"*."+f).toArray(String[]::new));
            fileChooser.setFileFilter(new FileNameExtensionFilter(desc,extensions));
        }
    }

    /**
     * <div lang="zh-cn">弹出文件打开对话框</div>
     * @param title <div lang="zh-cn">对话框标题</div>
     * @param extensions <div lang="zh-cn">过滤的扩展名，不以*或者*.开头，
     * 直接写扩展名即可。比如"jpg"、"png"</div>
     * @return <div lang="zh-cn">选择的文件的路径，如果没有选择文件而关闭对话框，则返回null。</div>
     */
    public static String fileOpenBox(String title,String... extensions)
    {
        showDummyFrameIfNeeded();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        setFileFilter(fileChooser,extensions);
        LocalConfigUtils.loadLastCurrentDir(fileChooser,"fileOpenBox");
        int ret = fileChooser.showOpenDialog(dummyFrame);
        if(ret!=JFileChooser.APPROVE_OPTION)
        {
            return null;
        }
        String fileName = fileChooser.getSelectedFile().toString();
        LocalConfigUtils.saveLastCurrentDir(fileChooser,"fileOpenBox");
        return fileChooser.getSelectedFile().toString();
    }

    /**
     * <div lang="zh-cn">弹出文件打开多文件对话框</div>
     * @param title <div lang="zh-cn">对话框标题</div>
     * @param extensions <div lang="zh-cn">过滤的扩展名，不以*或者*.开头，
     *  * 直接写扩展名即可。比如"jpg"、"png"</div>
     * @return <div lang="zh-cn">选择的多个文件的路径数组，如果没有选择文件而关闭对话框，则返回null。</div>
     */
    public static String[] filesOpenBox(String title,String... extensions)
    {
        showDummyFrameIfNeeded();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        fileChooser.setMultiSelectionEnabled(true);
        LocalConfigUtils.loadLastCurrentDir(fileChooser,"filesOpenBox");
        setFileFilter(fileChooser,extensions);
        int ret = fileChooser.showOpenDialog(dummyFrame);
        if(ret!=JFileChooser.APPROVE_OPTION)
        {
            return null;
        }
        LocalConfigUtils.saveLastCurrentDir(fileChooser,"filesOpenBox");
        return Arrays.stream(fileChooser.getSelectedFiles()).map(f->f.toString()).toArray(String[]::new);
    }

    private static JFileChooser createSaveJFileChooser()
    {
        JFileChooser fileChooser = new JFileChooser(){
            @Override
            public void approveSelection() {
                String confirmText = UIManager.getString("JFileChoose.saveExistConfirmText");
                File f = getSelectedFile();
                if (f.exists()) {
                    int result = JOptionPane.showConfirmDialog(this,
                            confirmText, null,
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    switch (result) {
                        case JOptionPane.YES_OPTION:
                            super.approveSelection();
                            return;
                        case JOptionPane.CANCEL_OPTION:
                            cancelSelection();
                            return;
                        default:
                            return;
                    }
                }
                super.approveSelection();
            }
        };
        return fileChooser;
    }

    /**
     * <div lang="zh-cn">弹出保存文件对话框，如果选择的文件已经存在，则提示【是否覆盖】</div>
     * @param title <div lang="zh-cn">对话框标题</div>
     * @param extensions <div lang="zh-cn">过滤的扩展名，不以*或者*.开头，
     *  * 直接写扩展名即可。比如"jpg"、"png"</div>
     * @return <div lang="zh-cn">选择的文件的路径，如果没有选择文件而关闭对话框，则返回null。</div>
     */
    public static String fileSaveBox(String title,String... extensions)
    {
        showDummyFrameIfNeeded();
        JFileChooser fileChooser = createSaveJFileChooser();
        fileChooser.setDialogTitle(title);
        setFileFilter(fileChooser,extensions);
        LocalConfigUtils.loadLastCurrentDir(fileChooser,"fileSaveBox");
        int ret = fileChooser.showSaveDialog(dummyFrame);
        if(ret!=JFileChooser.APPROVE_OPTION)
        {
            return null;
        }
        LocalConfigUtils.saveLastCurrentDir(fileChooser,"fileSaveBox");
        return fileChooser.getSelectedFile().toString();
    }

    /**
     * <div lang="zh-cn">弹出目录打开对话框</div>
     * @param title <div lang="zh-cn">对话框标题</div>
     * @return <div lang="zh-cn">选择的目录的路径，如果没有选择目录而关闭对话框，则返回null。</div>
     */
    public static String dirOpenBox(String title)
    {
        showDummyFrameIfNeeded();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        LocalConfigUtils.loadLastCurrentDir(fileChooser,"dirOpenBox");
        int ret = fileChooser.showOpenDialog(dummyFrame);
        if(ret!=JFileChooser.APPROVE_OPTION)
        {
            return null;
        }
        LocalConfigUtils.saveLastCurrentDir(fileChooser,"dirOpenBox");
        String dirName = fileChooser.getSelectedFile().toString();
        return dirName;
    }

    /**
     * <div lang="zh-cn">弹出目录保存的对话框，如果目录已经存在，则提示【是否覆盖】</div>
     * @param title <div lang="zh-cn">对话框标题</div>
     * @return <div lang="zh-cn">选择的目录的路径，如果没有选择目录而关闭对话框，则返回null。</div>
     */
    public static String dirSaveBox(String title)
    {
        showDummyFrameIfNeeded();
        JFileChooser fileChooser = createSaveJFileChooser();
        fileChooser.setDialogTitle(title);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        LocalConfigUtils.loadLastCurrentDir(fileChooser,"dirSaveBox");

        int ret = fileChooser.showSaveDialog(dummyFrame);
        if(ret!=JFileChooser.APPROVE_OPTION)
        {
            return null;
        }
        String dirName = fileChooser.getSelectedFile().toString();
        LocalConfigUtils.saveLastCurrentDir(fileChooser,"dirSaveBox");
        return dirName;
    }

    private static ProgressDialog progressDialog;

    /**
     * <div lang="zh-cn">显示进度显示对话框，非模态对话框。如果对话框关闭了，则再显示对话框。</div>
     * @param message <div lang="zh-cn">提示消息</div>
     * @param maximum <div lang="zh-cn">最大值</div>
     * @param value <div lang="zh-cn">当前值</div>
     */
    public static void showProgressDialog(Object message,int maximum,int value)
    {
        showDummyFrameIfNeeded();
        SwingUtilities.invokeLater(()->{
            if(progressDialog==null)
            {
                progressDialog = new ProgressDialog(dummyFrame);
                progressDialog.setModal(false);
                progressDialog.setLocationRelativeTo(null);//screen center
            }
            progressDialog.setMessage(CommonHelpers.toString(message));
            progressDialog.setProgress(maximum, value);
            progressDialog.pack();
            progressDialog.setVisible(true);
        });
    }

    /**
     * <div lang="zh-cn">关闭进度显示对话框</div>
     */
    public static void closeProgressDialog()
    {
        progressDialog.setVisible(false);
        progressDialog.dispose();
        progressDialog = null;
    }

    private static IndeterminateProgressDialog indeterminateProgressDialog;

    /**
     * <div lang="zh-cn">显示“非确定性”进度对话框，非模态对话框。如果对话框关闭了，则再显示对话框。</div>
     * @param message <div lang="zh-cn"></div>
     */
    public static void showIndeterminateProgressDialog(Object message)
    {
        showDummyFrameIfNeeded();
        SwingUtilities.invokeLater(()->{
            if(indeterminateProgressDialog==null)
            {
                indeterminateProgressDialog = new IndeterminateProgressDialog(dummyFrame);
                indeterminateProgressDialog.setModal(false);
                indeterminateProgressDialog.setLocationRelativeTo(null);//screen center
            }
            indeterminateProgressDialog.setMessage(CommonHelpers.toString(message));
            indeterminateProgressDialog.pack();
            indeterminateProgressDialog.setVisible(true);
        });
    }

    /**
     * <div lang="zh-cn">关闭“非确定性”进度对话框</div>
     */
    public static void closeIndeterminateProgressDialog()
    {
        indeterminateProgressDialog.setVisible(false);
        indeterminateProgressDialog.dispose();
        indeterminateProgressDialog = null;
    }

    /**
     * <div lang="zh-cn">获取屏幕尺寸</div>
     * @return <div lang="zh-cn"></div>
     */
    public static Dimension getScreenSize()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return screenSize;
    }

    /**
     * <div lang="zh-cn">获取屏幕宽度</div>
     * @return <div lang="zh-cn"></div>
     */
    public static int getScreenWidth()
    {
        Dimension screenSize = getScreenSize();
        return screenSize.width;
    }

    /**
     * <div lang="zh-cn">获取屏幕高度</div>
     * @return <div lang="zh-cn"></div>
     */
    public static int getScreenHeight()
    {
        Dimension screenSize = getScreenSize();
        return screenSize.height;
    }
}
