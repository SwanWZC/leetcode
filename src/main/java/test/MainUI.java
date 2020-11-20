//package test;
//
//import java.awt.FlowLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import javax.swing.JButton;
//import javax.swing.JComboBox;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JTextField;
//
//public class MainUI extends JFrame {
//
//    private static final long serialVersionUID = 518541171932584910L;
//    private JTextField jtf1;
//    private JTextField jtf2;
//    private JComboBox<String> jcb1;
//    private JComboBox<String> jcb2;
//    private JButton jbu;
//    private ArrayList<File> filelist = new ArrayList<>();// 存储文件队列
//    //主函数
//    public static void main(String[] args) {
//        MainUI mui = new MainUI();
//        mui.init();
//        mui.listen();
//    }
//
//    // 界面设计
//    public void init() {
//        setTitle("文件转码");
//        setSize(340, 210);
//        setLocationRelativeTo(null);
//        setLayout(new FlowLayout());
//        setResizable(false);
//        setDefaultCloseOperation(3);
//        // 原文件编码标签
//        JLabel jl2 = new JLabel("原文件编码");
//        add(jl2);
//        // 支持的编码格式
//        String[] encodeTypes = { "GBK", "UTF-8", "Unicode", "ASCII",  "GB2312" };
//        // 原文件编码
//        jcb1 = new JComboBox<>(encodeTypes);
//        add(jcb1);
//        // 目标文件编码标签
//        JLabel jl4 = new JLabel("目标文件编码");
//        add(jl4);
//        // 目标文件编码
//        jcb2 = new JComboBox<>(encodeTypes);
//        add(jcb2);
//        // 原文件地址标签
//        JLabel jl1 = new JLabel("原文件地址");
//        add(jl1);
//        // 原文件地址
//        jtf1 = new JTextField(28);
//        add(jtf1);
//        // 目标文件地址标签
//        JLabel jl3 = new JLabel("目标文件地址");
//        add(jl3);
//        // 目标文件地址
//        jtf2 = new JTextField(28);
//        add(jtf2);
//        // 开始按钮
//        jbu = new JButton("开始");
//        add(jbu);
//        setVisible(true);
//    }
//
//    // 监听器获取信息
//    private void listen() {
//        // 添加监听器
//        jbu.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // 获取地址、编码信息
//                String originalDir = jtf1.getText();
//                String targetDir = jtf2.getText();
//                String type1 = jcb1.getSelectedItem().toString();
//                String type2 = jcb2.getSelectedItem().toString();
//                // 调用文件判断方法及编码方法
//                if (judge(originalDir, targetDir)) {
//                    encode(type1, type2, targetDir);
//                }
//            }
//        });
//    }
//
//    // 对原文件进行判断
//    private Boolean judge(String originalDir, String targetDir) {
//        File file1 = new File(originalDir);
//        // 判断原文件是否存在
//        while (!file1.exists()) {
//            JOptionPane.showMessageDialog(null, "输入文件不存在");
//            return false;
//        }
//        if (file1.isFile()) {
//            filelist.add(file1);
//            // 创建新的File对象，指定存储位置
//            File file2 = new File(targetDir);
//            // 文件目录存在则创建
//            if (!file2.exists()) {
//                file2.mkdir();
//            }
//        } else {
//            // 创建新的File对象，指定存储位置
//            File file2 = new File(targetDir, originalDir);
//            // 调用遍历文件方法
//            getFile(file1, file2);
//        }
//        return true;
//    }
//
//    // 遍历文件夹
//    private void getFile(File dir1, File dir2) {
//        // 文件夹不存在，则创建文件
//        if (!dir2.exists()) {
//            dir2.mkdirs();
//        }
//        // 获取该目录下文件数组
//        File[] files = dir1.listFiles();
//        // 遍历文件数组
//        for (File file : files) {
//            if (file.isDirectory()) {
//                // 创建新的文件对象，指定保存路径
//                File dir = new File(dir2, file.getName());
//                // 递归调用
//                getFile(file, dir);
//            } else {
//                // 添加至文件队列中
//                filelist.add(file);
//            }
//        }
//    }
//
//    // 编码方法
//    private void encode(String type1, String type2, String targetDir) {
//        // 给队列中文件编码
//        for (File file : filelist) {
//            File file3;
//            // 按原文件结构指定新文件的路径
//            if (file.getParent() != null) {
//                String pathT = file.getParent();
//                pathT = pathT.substring(27,pathT.length());
//                File filetmp = new File(targetDir + "\\" + pathT);
//                if (!filetmp.exists()){
//                    filetmp.mkdirs();
//                }
//                file3 = new File(targetDir + "\\" + pathT, file.getName());
//            } else {
//                file3 = new File(targetDir, file.getName());
//            }
//            try {
//                // 创建指定编码格式的字节转换流
//                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), type1);
//                OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file3), type2);
//                // 使用缓冲流对文件进行读写
//                BufferedReader br = new BufferedReader(isr);
//                BufferedWriter bw = new BufferedWriter(osw);
//                String line;
//                // 读写文件
//                while ((line = br.readLine()) != null) {
//                    bw.write(line);
//                    bw.flush();
//                    bw.newLine();
//                }
//                br.close();// 关闭输入流
//                bw.close();// 关闭输出流
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        // 编码完成提示信息
//        JOptionPane.showMessageDialog(null, "编码完成！");
//    }
//}