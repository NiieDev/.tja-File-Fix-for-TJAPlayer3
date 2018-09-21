package me.niie.main;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main extends JFrame {

	private JPanel contentPane;

	private static String title = "TJAPlayer3用 .tja Fix";
	private static String ver = "1.0";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {

		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);

		            Dimension windowSize = frame.getSize();
		            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		            Point centerPoint = ge.getCenterPoint();

		            int dx = centerPoint.x - windowSize.width / 2;
		            int dy = centerPoint.y - windowSize.height / 2;
		            frame.setLocation(dx, dy);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setTitle(title + " " + ver);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 748, 325);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("TJAPlayer3用 .tja Fix");
		lblNewLabel.setFont(new Font("MS UI Gothic", Font.PLAIN, 44));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(12, 10, 708, 84);
		contentPane.add(lblNewLabel);

		JLabel lblInput = new JLabel("input");
		lblInput.setHorizontalAlignment(SwingConstants.CENTER);
		lblInput.setFont(new Font("MS UI Gothic", Font.PLAIN, 20));
		lblInput.setBounds(12, 124, 708, 28);
		contentPane.add(lblInput);

		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
		textArea.setBounds(12, 162, 627, 28);
		contentPane.add(textArea);

		JButton btnNewButton = new JButton("...");
		btnNewButton.setBounds(651, 162, 69, 28);
		contentPane.add(btnNewButton);

		btnNewButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				fileselect(textArea);
			}
		});

		JButton btnExit = new JButton("Exit");
		btnExit.setFont(new Font("MS UI Gothic", Font.PLAIN, 48));
		btnExit.setBounds(376, 213, 344, 48);
		contentPane.add(btnExit);
		btnExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				System.exit(0);
			}
		});

		JButton btnFix = new JButton("Fix");
		btnFix.setFont(new Font("MS UI Gothic", Font.PLAIN, 48));
		btnFix.setBounds(12, 213, 344, 48);
		contentPane.add(btnFix);
		btnFix.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				String area = textArea.getText();

				if(!area.endsWith(".tja")) {
					String message = "ファイルパスが指定されていません";
					System.out.println(message);
					JOptionPane.showMessageDialog(null, message);
					return;
				}
				int error = 0;
				File file = new File((new File(textArea.getText()).getParent() + "\\output.tja").toString());
				if (checkfile(file)){
					file.delete();
				}
				try {
					file.createNewFile();
				} catch (IOException e1) {
					error = 101;
					e1.printStackTrace();
				}
				if(area.endsWith(".tja")) {
					boolean start = false;
					List<String> input = loadTja(area);
					List<String> output = new ArrayList<String>();
					int count = 0;
					for(String str : input) {
						String str_2 = str.toUpperCase();
						if (str_2.equals("#START")) {
							start = true;
						}else if (str_2.equals("#END")) {
							start = false;
						}
						if(str_2.startsWith("STYLE:")) {
							if(!start) {
								count++;
								continue;
							}
						}
						if(!str_2.startsWith("#SCROLL")
								&& !str_2.startsWith("#GOGOSTART")
								&& !str_2.startsWith("#GOGOEND")
								&& !str_2.startsWith("#MEASURE")
								&& !str_2.startsWith("#DELAY")
								&& !str_2.startsWith("#SECTION")
								&& !str_2.startsWith("#BRANCHSTART")
								&& !str_2.startsWith("#BRANCHEND")
								&& !str_2.startsWith("#N")
								&& !str_2.startsWith("#M")
								&& !str_2.startsWith("#E")
								&& !str_2.startsWith("#LEVELHOLD")
								&& !str_2.startsWith("#BMSCROLL")
								&& !str_2.startsWith("#HBSCROLL")
								&& !str_2.startsWith("#BARLINEOFF")
								&& !str_2.startsWith("#BARLINEON")
								&& !str_2.startsWith("#BPMCHANGE")
								&& !str_2.startsWith("#START")
								&& !str_2.startsWith("//")
								&& !str_2.startsWith("#END")
								) {
							if(str_2.contains(" ")) {
								if(start) {
									str = str.replace(" ", "");
									count++;
								}
							}
						}
						if(str_2.startsWith("#SCROLL") && !str_2.startsWith("#SCROLL ")){
							str = str.replace("#SCROLL", "#SCROLL ");
							count++;
						}
						if(str_2.contains("#SCROLL0")
								|| str_2.contains("#SCROLL1")
								|| str_2.contains("#SCROLL2")
								|| str_2.contains("#SCROLL3")
								|| str_2.contains("#SCROLL4")
								|| str_2.contains("#SCROLL5")
								|| str_2.contains("#SCROLL6")
								|| str_2.contains("#SCROLL7")
								|| str_2.contains("#SCROLL8")
								|| str_2.contains("#SCROLL9")
								|| str_2.contains("#SCROLL-")
								){
							str = str.replace("#SCROLL-0", "\n#SCROLL -0\n");
							str = str.replace("#SCROLL-1", "\n#SCROLL -1\n");
							str = str.replace("#SCROLL-2", "\n#SCROLL -2\n");
							str = str.replace("#SCROLL-3", "\n#SCROLL -3\n");
							str = str.replace("#SCROLL-4", "\n#SCROLL -4\n");
							str = str.replace("#SCROLL-5", "\n#SCROLL -5\n");
							str = str.replace("#SCROLL-6", "\n#SCROLL -6\n");
							str = str.replace("#SCROLL-7", "\n#SCROLL -7\n");
							str = str.replace("#SCROLL-8", "\n#SCROLL -8\n");
							str = str.replace("#SCROLL-9", "\n#SCROLL -9\n");
							str = str.replace("#SCROLL0", "\n#SCROLL 0\n");
							str = str.replace("#SCROLL1", "\n#SCROLL 1\n");
							str = str.replace("#SCROLL2", "\n#SCROLL 2\n");
							str = str.replace("#SCROLL3", "\n#SCROLL 3\n");
							str = str.replace("#SCROLL4", "\n#SCROLL 4\n");
							str = str.replace("#SCROLL5", "\n#SCROLL 5\n");
							str = str.replace("#SCROLL6", "\n#SCROLL 6\n");
							str = str.replace("#SCROLL7", "\n#SCROLL 7\n");
							str = str.replace("#SCROLL8", "\n#SCROLL 8\n");
							str = str.replace("#SCROLL9", "\n#SCROLL 9\n");
							count= count + replaces(str, "#SCROLL");
						}
						if(str_2.startsWith("SCOREINIT:")){
							if(!start) {
								if(tja(str, "SCOREINIT").replace(" ", "").length() != 0) {
									double scoreinit = Double.parseDouble(tja(str, "SCOREINIT").replace(" ", ""));
									if(scoreinit > 32767) {
										str = "SCOREINIT:32767";
										count++;
									}
								}
							}
						}
						if(str_2.startsWith("SCOREDIFF:")){
							if(!start) {
								if(tja(str, "SCOREDIFF").replace(" ", "").length() != 0) {
									double scorediff = Double.parseDouble(tja(str, "SCOREDIFF").replace(" ", ""));
									if(scorediff > 32767) {
										str = "SCOREDIFF:32767";
										count++;
									}
								}
							}
						}
						System.out.println(str);
						output.add(str);
						/*
						try {
							writefile(str, (new File(textArea.getText()).getParent() + "\\output.tja").toString());
						} catch (IOException e) {
							error = 102;
							break;
						}
						*/
					}
					try {
						writefile(output, (new File(textArea.getText()).getParent() + "\\output.tja").toString());
					} catch (IOException e) {
						if(error == 0) {
							error = 102;
						}
					}
					if(error == 101) {
						String message = "ファイル作成中にエラーが発生しました";
						System.out.println(message);
						JOptionPane.showMessageDialog(null, message);
						return;
					}
					else if(error == 102) {
						String message = "ファイル書き込み中にエラーが発生しました";
						System.out.println(message);
						JOptionPane.showMessageDialog(null, message);
						return;
					}else {
						String message = "問題は" + count + "個見つかり、すべて修正されました";
						System.out.println(message);
						JOptionPane.showMessageDialog(null, message);
					}
					if(error != 0) {

					}
				}
			}
		});

	}

    private static int replaces(String text, String str) {
        int result = Integer.MAX_VALUE;
        int count = text.length() - text.replace(str, "").length();
        result = Math.min(result, count);
        return result / str.length();
    }

	private String tja(String line, String str) {
		if(line.startsWith(str + ":")) {
			return line.substring(str.length() + 1);

		}else {
			return null;
		}
	}
	public static Path getApplicationPath(Class<?> cls) throws URISyntaxException {
		ProtectionDomain pd = cls.getProtectionDomain();
		CodeSource cs = pd.getCodeSource();
		URL location = cs.getLocation();
		URI uri = location.toURI();
		Path path = Paths.get(uri);
		return path;
	}
	private void writefile(List<String> list, String path) throws IOException {
		File file = new File(path);

		if (!checkfile(file)){
			file.createNewFile();
		}
		if (checkfile(file)){
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path),"Shift_JIS")));

            for(String str : list) {
            	pw.println(str);
            }

            pw.close();
		}else{
			System.out.println("ファイルが存在しません");
		}
	}
	/*
	private void writefile(String str, String path) throws IOException {
		File file = new File(path);

		if (!checkfile(file)){
			file.createNewFile();
		}
		if (checkfile(file)){
            FileWriter fw = new FileWriter(path, true);
            // PrintWriterクラスのオブジェクトを生成する
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

            //ファイルに追記する
            pw.println(str);

            //ファイルを閉じる
            pw.close();
		}else{
			System.out.println("ファイルが存在しません");
		}
	}
	*/
	  private static boolean checkfile(File file){
		  if(file.exists()) {
			  if(file.isFile() && file.canWrite()) {
				  return true;
			  }
		  }
		  return false;
	  }

	private List loadTja(String path) {
		try {
			File file = new File(path);
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"SJIS"));

			String line;

			List<String> list = new ArrayList<String>();

			while((line = reader.readLine()) != null) {
				list.add(line);
			}
			reader.close();

			return list;
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return null;

	}
	private void fileselect(JTextArea textarea) {
    	JFileChooser filechooser = new JFileChooser(textarea.getText());
		filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		FileFilter filter = new FileNameExtensionFilter("TJAファイル", "tja");
		filechooser.addChoosableFileFilter(filter);
	    int selected = choose(filechooser);
	    if (selected == JFileChooser.APPROVE_OPTION){
	      File file = filechooser.getSelectedFile();
	      textarea.setText(file.getPath());
	    }else if (selected == JFileChooser.CANCEL_OPTION){
	    }else if (selected == JFileChooser.ERROR_OPTION){
	    }
	}
	public int choose(JFileChooser filechooser) {
		return filechooser.showOpenDialog(this);
	}
}
