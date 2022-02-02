import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Arrays;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import java.io.*;
public class Mole extends JFrame implements ActionListener{
	JButton[] btn=new JButton[9];
	int[] moles= new int[9];
	private Timer timer;
	private int gameTime=30;
	private int whacked = 0;
	private int missed=0;
	private int score=0;
	Boolean gameStarted = false;
	public static void main(String[] args){
		new Mole();
	}
	public Mole(){
		super("Whack-a-mole");
		JPanel topBoard = new JPanel(new BorderLayout());
		JLabel timeJLabel = new JLabel();
		JLabel scoreJLabel = new JLabel();
		timeJLabel.setText(String.valueOf(gameTime) + " :Time ");
		scoreJLabel.setText("Score=> " + String.valueOf(score));
		topBoard.add(scoreJLabel,BorderLayout.CENTER);
		topBoard.add(timeJLabel,BorderLayout.EAST);
		JButton startBtn = new JButton("Start Play");
		startBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				startBtn.setEnabled(false);
				gameStarted=true;
				timer.start();
			}
		});
		topBoard.add(startBtn,BorderLayout.WEST);
		JPanel gameBoard = new JPanel(new GridLayout(3,3));
		for (int i=0;i<9;i++){
			btn[i] = new JButton();
			btn[i].setFocusable(false);
			btn[i].setBackground(new Color(104,69,5));
			gameBoard.add(btn[i]);
			btn[i].addActionListener(this);
		}
		add(topBoard,BorderLayout.NORTH);
		add(gameBoard,BorderLayout.CENTER);
		setSize(500,500);
		setVisible(true);
		setResizable(false);
		timer = new Timer(600, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(gameTime<=0){
					timer.stop();
					gameOver();
					startBtn.setEnabled(true);
				}else{
					genMole();
					gameTime --;
					timeJLabel.setText(String.valueOf(gameTime) + " :Time ");
					scoreJLabel.setText( "Score=> "+ String.valueOf(score));
				}
			}
		});
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public void actionPerformed(ActionEvent e){
		JButton temp = (JButton)e.getSource();
		if (moles[Arrays.asList(btn).indexOf(temp)]==1){
			clear();
			playSound("Slap.wav");
			whacked++;
			missed--;
			score += 50;
		}
		else {
			if (gameStarted)
				playSound("laugh.wav");		
		}
	}
	private void genMole(){
		Random rnd = new Random(System.currentTimeMillis()); 
		clear();
		int moleLoc = rnd.nextInt(9);
		moles[moleLoc] = 1;
		btn[moleLoc].setIcon(loadImage("Mole.jpg"));
		missed++;	
	}
	private void clear(){
		for (int i=0;i<9;i++){
			moles[i]=0;
			btn[i].setIcon(null);
		}
	}
	private void gameOver(){
		clear();
		JOptionPane.showMessageDialog(this,"Your score is " + score + " hit "+ whacked + " and missed " + missed + " times!" ,"Game Over",JOptionPane.INFORMATION_MESSAGE);
		whacked=0;
		missed=0;
		score=0;
		gameTime=30;
		gameStarted=false;
	}
	private ImageIcon loadImage(String path){
        Image image = new ImageIcon(this.getClass().getResource("Mole.jpg")).getImage();
        Image scaledImage = image.getScaledInstance(132, 132, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
	public void playSound(String soundName)
	{
	   try {
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
		Clip clip = AudioSystem.getClip( );
		clip.open(audioInputStream);
		clip.start( );
	   }
	   catch(Exception ex){
		 System.out.println("Error with playing sound.");
	   }
	}
}
