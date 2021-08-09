package app.dsm.graph.learn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

public class JFrameL {

    public static JFrame frame= null;

    private void showFrame(){
        //更改外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        frame = new JFrame("learn01");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1440,900);


        JButton jb = new JButton("jb1");
        jb.addActionListener(new ButtonListener());
        frame.setLayout(new FlowLayout());
        frame.add(jb);

        JLabel fps = new JLabel();
        fps.setName("fps");
        frame.add(BorderLayout.CENTER,fps);

        frame.setVisible(true);
        // FPS:60
        new Thread(new Runnable() {
            private JLabel fps = null;
            int f =0;
            long st = System.currentTimeMillis();
            long last = 0;
            @Override
            public void run() {
                try {
                    //
                    JProgressBar jpb = new JProgressBar();
                    jpb.setStringPainted(true);
                    jpb.setMinimum(1);
                    jpb.setMaximum(60);

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            frame.add(jpb);
                        }
                    });
                    //

                    for(Component c : frame.getRootPane().getContentPane().getComponents()){
                        if(c.getName()=="fps"){
                            fps = (JLabel) c;
                            break;
                        }
                    }
                    while (true){
                        if(f==0){
                            st = System.currentTimeMillis();
                        }
                        if(last >= 1000){
                            st = System.currentTimeMillis();
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    fps.setText(String.valueOf(f));
                                    f = 0;
                                }
                            });
                        }
                        //TimeUnit.NANOSECONDS.sleep(1);
                        TimeUnit.MILLISECONDS.sleep(1000/60);
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                jpb.setValue(f);
                                frame.revalidate();
                                f++;
                                last = System.currentTimeMillis() - st;
                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
//        try {
//            TimeUnit.SECONDS.sleep(2);
//            //frame.dispose();  //退出
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrameL jfl = new JFrameL();
                jfl.showFrame();
            }
        });

    }
}

class ButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JButton jb2 = new JButton("jb2");
                JFrameL.frame.add(jb2);
                //JFrameL.frame.revalidate();
            }
        });
    }
}
