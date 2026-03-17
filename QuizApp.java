import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

class UIStyle {
    public static final Color BG_DARK = new Color(24, 26, 27);
    public static final Color BG_PANEL = new Color(35, 39, 42);
    public static final Color ACCENT_GREEN = new Color(46, 204, 113); // High visibility emerald
    public static final Color TEXT_WHITE = new Color(240, 240, 240);
    public static final Color TEXT_CYAN = new Color(0, 255, 255);
}

class QuizQuestion {
    String text, a, b, c, d, correct;
    public QuizQuestion(String t, String a, String b, String c, String d, String cr) {
        this.text = t; this.a = a; this.b = b; this.c = c; this.d = d; this.correct = cr;
    }
}

class QuizLogin extends JDialog {
    public int userId = -1;
    private JTextField txtUser;
    private JPasswordField txtPass;
    private boolean succeeded = false;

    public QuizLogin(JFrame parent) {
        super(parent, "Quiz Pro Login", true);
        getContentPane().setBackground(UIStyle.BG_DARK);
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("USER LOGIN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(UIStyle.ACCENT_GREEN);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitle, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1;
        JLabel l1 = new JLabel("Username:"); l1.setForeground(UIStyle.TEXT_WHITE);
        add(l1, gbc);
        txtUser = new JTextField(15);
        txtUser.setBackground(Color.BLACK); txtUser.setForeground(Color.WHITE);
        txtUser.setCaretColor(Color.WHITE);
        gbc.gridx = 1; add(txtUser, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel l2 = new JLabel("Password:"); l2.setForeground(UIStyle.TEXT_WHITE);
        add(l2, gbc);
        txtPass = new JPasswordField(15);
        txtPass.setBackground(Color.BLACK); txtPass.setForeground(Color.WHITE);
        gbc.gridx = 1; add(txtPass, gbc);

        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setBackground(UIStyle.ACCENT_GREEN);
        btnLogin.setForeground(Color.BLACK); // Dark text on bright background
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(btnLogin, gbc);

        btnLogin.addActionListener(e -> {
            try (Connection conn = DBConfig.getConnection()) {
                PreparedStatement ps = conn.prepareStatement("SELECT user_id FROM users WHERE username=? AND password=?");
                ps.setString(1, txtUser.getText());
                ps.setString(2, new String(txtPass.getPassword()));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    userId = rs.getInt("user_id");
                    succeeded = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Access Denied!");
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        setSize(400, 350);
        setLocationRelativeTo(parent);
    }
    public boolean isSucceeded() { return succeeded; }
}

public class QuizApp extends JFrame {
    private JLabel lblQuestion, lblTimer, lblCount;
    private JRadioButton[] options = new JRadioButton[4];
    private ButtonGroup group;
    private JButton btnNext;
    private ArrayList<QuizQuestion> questionList = new ArrayList<>();
    private int currentIndex = 0, score = 0, timeLeft = 15, currentUserId;
    private Timer timer;

    public QuizApp(int userId) {
        this.currentUserId = userId;
        setTitle("Interactive Quiz - Java Edition");
        setSize(750, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIStyle.BG_DARK);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIStyle.BG_PANEL);
        header.setBorder(new EmptyBorder(15, 25, 15, 25));
        
        lblCount = new JLabel("Question 1 of 5");
        lblCount.setForeground(UIStyle.TEXT_WHITE);
        lblTimer = new JLabel("⏱ 15s", SwingConstants.RIGHT);
        lblTimer.setForeground(Color.YELLOW);
        lblTimer.setFont(new Font("Monospaced", Font.BOLD, 22));
        
        header.add(lblCount, BorderLayout.WEST);
        header.add(lblTimer, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Body
        JPanel center = new JPanel(new GridLayout(6, 1, 15, 15));
        center.setBackground(UIStyle.BG_DARK);
        center.setBorder(new EmptyBorder(30, 60, 30, 60));

        lblQuestion = new JLabel("Loading...");
        lblQuestion.setForeground(UIStyle.TEXT_CYAN);
        lblQuestion.setFont(new Font("Segoe UI", Font.BOLD, 22));
        center.add(lblQuestion);

        group = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setBackground(UIStyle.BG_DARK);
            options[i].setForeground(UIStyle.TEXT_WHITE);
            options[i].setFont(new Font("Segoe UI", Font.PLAIN, 18));
            group.add(options[i]);
            center.add(options[i]);
        }
        add(center, BorderLayout.CENTER);

        // High Visibility Next Button
        btnNext = new JButton("NEXT STEP →");
        btnNext.setBackground(UIStyle.ACCENT_GREEN);
        btnNext.setForeground(Color.BLACK);
        btnNext.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnNext.setPreferredSize(new Dimension(0, 70));
        add(btnNext, BorderLayout.SOUTH);

        loadQuestionsFromDB();
        displayQuestion();

        timer = new Timer(1000, e -> {
            timeLeft--;
            lblTimer.setText("⏱ " + String.format("%02d", timeLeft) + "s");
            if (timeLeft <= 0) { checkAnswer(); nextQuestion(); }
        });
        timer.start();

        btnNext.addActionListener(e -> { checkAnswer(); nextQuestion(); });
        setVisible(true);
    }

    private void loadQuestionsFromDB() {
        try (Connection conn = DBConfig.getConnection()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM questions ORDER BY RAND() LIMIT 5");
            while (rs.next()) {
                questionList.add(new QuizQuestion(rs.getString("question_text"), 
                    rs.getString("option_a"), rs.getString("option_b"), 
                    rs.getString("option_c"), rs.getString("option_d"), 
                    rs.getString("correct_answer")));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void displayQuestion() {
        if (currentIndex < questionList.size()) {
            QuizQuestion q = questionList.get(currentIndex);
            lblCount.setText("PROGRESS: " + (currentIndex + 1) + " / " + questionList.size());
            lblQuestion.setText("<html><div style='width: 500px;'>" + q.text + "</div></html>");
            options[0].setText(q.a); options[1].setText(q.b);
            options[2].setText(q.c); options[3].setText(q.d);
            group.clearSelection();
            timeLeft = 15;
        } else { endQuiz(); }
    }

    private void checkAnswer() {
        if (currentIndex >= questionList.size()) return;
        String ans = "";
        if (options[0].isSelected()) ans = "A";
        else if (options[1].isSelected()) ans = "B";
        else if (options[2].isSelected()) ans = "C";
        else if (options[3].isSelected()) ans = "D";
        if (ans.equals(questionList.get(currentIndex).correct)) score++;
    }

    private void nextQuestion() {
        currentIndex++;
        displayQuestion();
    }

    private void endQuiz() {
        timer.stop();
        try (Connection conn = DBConfig.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO results (user_id, score, total_questions) VALUES (?, ?, ?)");
            ps.setInt(1, currentUserId);
            ps.setInt(2, score);
            ps.setInt(3, questionList.size());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        
        JOptionPane.showMessageDialog(this, "Game Over!\nFinal Score: " + score + "/" + questionList.size());
        System.exit(0);
    }

    public static void main(String[] args) {
        QuizLogin login = new QuizLogin(null);
        login.setVisible(true);
        if (login.isSucceeded()) { new QuizApp(login.userId); }
        else { System.exit(0); }
    }
}
