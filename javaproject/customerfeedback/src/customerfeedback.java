import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class customerfeedback {

    private JFrame frame;
    private JPanel mainPanel;
    private JTable feedbackTable;
    private DefaultTableModel tableModel;

    private int selectedRating = 0;
    private JButton[] ratingButtons = new JButton[5];
    private String[] ratingTexts = {"1", "2", "3", "4", "5"};

    private JTextField txtName, txtEmail;
    private JTextArea txtReview;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new customerfeedback().createDashboard());
    }

    public void createDashboard() {
        frame = new JFrame("Feedback Hub");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());


        JPanel navPanel = new JPanel(null);
        navPanel.setPreferredSize(new Dimension(200, 500));
        navPanel.setBackground(new Color(0, 102, 153));

        JButton btnSubmit = makeNavButton("Submit", 160);
        JButton btnView = makeNavButton("View Reviews", 220);

        navPanel.add(btnSubmit);
        navPanel.add(btnView);

        mainPanel = new JPanel(new CardLayout());
        JPanel formPanel = createFormPanel();
        JPanel tablePanel = createTablePanel("All Reviews");

        mainPanel.add(formPanel, "Form");
        mainPanel.add(tablePanel, "Table");

        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCard("Form");
            }
        });

        btnView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCard("Table");
                loadFeedbacks();
            }
        });

        frame.add(navPanel, BorderLayout.WEST);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JButton makeNavButton(String text, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(20, y, 160, 40);
        btn.setBackground(new Color(0, 153, 153));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(new Color(245, 245, 245));

        JLabel lblTitle = new JLabel("Submit Review");
        lblTitle.setFont(new Font("Verdana", Font.BOLD, 22));
        lblTitle.setBounds(250, 20, 300, 30);
        formPanel.add(lblTitle);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(100, 100, 100, 25);
        txtName = new JTextField();
        txtName.setBounds(220, 100, 250, 30);
        formPanel.add(lblName);
        formPanel.add(txtName);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(100, 150, 100, 25);
        txtEmail = new JTextField();
        txtEmail.setBounds(220, 150, 250, 30);
        formPanel.add(lblEmail);
        formPanel.add(txtEmail);

        JLabel lblReview = new JLabel("Review:");
        lblReview.setBounds(100, 200, 100, 25);
        txtReview = new JTextArea();
        txtReview.setLineWrap(true);
        txtReview.setWrapStyleWord(true);
        JScrollPane reviewScroll = new JScrollPane(txtReview);
        reviewScroll.setBounds(220, 200, 250, 100);
        formPanel.add(lblReview);
        formPanel.add(reviewScroll);

        JLabel lblRating = new JLabel("Rating:");
        lblRating.setBounds(100, 320, 100, 25);
        formPanel.add(lblRating);

        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        ratingPanel.setBounds(220, 320, 400, 40);
        for (int i = 0; i < 5; i++) {
            final int index = i + 1;
            ratingButtons[i] = new JButton(ratingTexts[i]);
            ratingButtons[i].setFocusPainted(false);
            ratingButtons[i].setFont(new Font("Tahoma",Font.PLAIN,12));
            ratingButtons[i].setMargin(new Insets(2,2,2,2));
            ratingButtons[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setRating(index);
                }
            });
            ratingPanel.add(ratingButtons[i]);
        }
        formPanel.add(ratingPanel);

        JButton submitBtn = new JButton("Submit");
        submitBtn.setBounds(220, 380, 150, 40);
        submitBtn.setBackground(new Color(0, 153, 76));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                submitFeedback();
            }
        });
        formPanel.add(submitBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setBounds(380, 380, 120, 40);
        clearBtn.setBackground(new Color(204, 0, 51));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        formPanel.add(clearBtn);

        return formPanel;
    }

    private JPanel createTablePanel(String title) {
        JPanel tablePanel = new JPanel(new BorderLayout());
        JLabel tableTitle = new JLabel(title, SwingConstants.CENTER);
        tableTitle.setFont(new Font("Verdana", Font.BOLD, 20));
        tablePanel.add(tableTitle, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Email", "Review", "Rating", "Time"};
        tableModel = new DefaultTableModel(columns, 0);
        feedbackTable = new JTable(tableModel);
        tablePanel.add(new JScrollPane(feedbackTable), BorderLayout.CENTER);

        return tablePanel;
    }

    private void showCard(String cardName) {
        ((CardLayout) mainPanel.getLayout()).show(mainPanel, cardName);
    }

    private void setRating(int index) {
        selectedRating = index;
        for (int i = 0; i < ratingButtons.length; i++) {
            if (i == index - 1) {
                ratingButtons[i].setBackground(Color.GREEN);
                ratingButtons[i].setForeground(Color.WHITE);
            } else {
                ratingButtons[i].setBackground(null);
                ratingButtons[i].setForeground(Color.BLACK);
            }
        }
    }

    private void clearForm() {
        txtName.setText("");
        txtEmail.setText("");
        txtReview.setText("");
        selectedRating = 0;
        for (JButton btn : ratingButtons) {
            btn.setBackground(null);
            btn.setForeground(Color.BLACK);
        }
    }

    private void submitFeedback() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String review = txtReview.getText().trim();

        if (name.isEmpty() || email.isEmpty() || review.isEmpty() || selectedRating == 0) {
            JOptionPane.showMessageDialog(frame, "Please fill all fields and select rating");
            return;
        }

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/connection", "root", "Sana224100")) {
            String sql = "INSERT INTO customers (name,email,review,rating) VALUES (?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, review);
            ps.setInt(4, selectedRating);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Review Submitted!");
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Database Error: " + e.getMessage());
        }
    }

    private void loadFeedbacks() {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/connection", "root", "Sana224100")) {
            String sql = "SELECT * FROM customers";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("review"),
                        rs.getInt("rating"),
                        rs.getTimestamp("time")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Database Error: " + e.getMessage());
        }
    }
}


