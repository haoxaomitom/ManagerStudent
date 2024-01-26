/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package asmjava3.view;

import asmjava3.view.model.Session;
import asmjava3.view.model.SinhVien;
import asmjava3.view.model.SinhVienDAO;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author soaic
 */
public class SinhVienDialog extends javax.swing.JFrame {

    SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
    SinhVienDAO dao = new SinhVienDAO();
    String strHinhAnh = null;
    private String fullname;
    private String email;
    int index = 0;
    private DefaultTableModel tblModel;
    private Icon currentIcon;
    private int currentRow = -1;
    private int totalRows = -1;

    public SinhVienDialog() {
        initComponents();
        setLocationRelativeTo(null);
        fillDataTABLE();
        updateTotalStudent();
        setCurrentRowAndTotalRows();
        String iconPath = "D://StudyClollege//SOF203_Java3//Assignment//asmjava3//src//asmjava3//img//sms-logo.png";
        Image iconImage = new ImageIcon(iconPath).getImage();
        setIconImage(iconImage);
        setTitle("Quản lí Sinh Viên");

    }

    public void reset() {
        txtMasv.setText("");
        txtDiachi.setText("");
        txtNgaysinh.setText("");
        txtHoten.setText("");
        rdoNam.isSelected();
        lblHinhanh.setIcon(null);
        strHinhAnh = null;
    }

    public boolean validateSave() {
        if (txtMasv.getText().isEmpty() || txtHoten.getText().isEmpty() || txtNgaysinh.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All infomation need to fill !");
            return false;
        }
        // Kiểm tra xem mã sinh viên đã tồn tại trong bảng tblSinhVien chưa
        String maSVToCheck = txtMasv.getText();
        int columnIndex = 0; // Vị trí cột mã sinh viên trong bảng tblSinhVien
        int rowIndex = getRowByValue(tblSinhVien, maSVToCheck, columnIndex);
        if (rowIndex != -1) {
            JOptionPane.showMessageDialog(this, "Student ID already exists!");
            return false;
        }
        String birth = txtNgaysinh.getText();
        try {
            Date ngaySinh = date_format.parse(birth);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please enter date in the format dd/MM/yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
            txtNgaysinh.setText("");
            txtNgaysinh.requestFocus();
        }
        return true;
    }

    public boolean validateUpdate() {
        if (txtMasv.getText().isEmpty() || txtHoten.getText().isEmpty() || txtNgaysinh.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All infomation need to fill !");
            return false;
        }
        String birth = txtNgaysinh.getText();
        try {
            Date ngaySinh = date_format.parse(birth);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please enter date in the format dd/MM/yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
            txtNgaysinh.setText("");
            txtNgaysinh.requestFocus();
        }
        return true;
    }

    public SinhVien getModel() throws ParseException {
        SinhVien sv = new SinhVien();
        sv.setMasv(txtMasv.getText());
        sv.setTenSV(txtHoten.getText());
        boolean gt = false;
        if (rdoNam.isSelected()) {
            gt = true;
        }
        sv.setGioiTinh(gt);
        sv.setDiaChi(txtDiachi.getText());
        sv.setNgaySinh(date_format.parse(txtNgaysinh.getText()));
        if (strHinhAnh == null) {
            sv.setHinhAnh("No img");
        } else {
            sv.setHinhAnh(strHinhAnh);
        }
        return sv;
    }

    public void fillDataTABLE() {
        DefaultTableModel model = (DefaultTableModel) tblSinhVien.getModel();
        model.setRowCount(0);
        for (SinhVien sv : dao.getAllSinhVien()) {
            Object rowData[] = new Object[6];
            rowData[0] = sv.getMasv();
            rowData[1] = sv.getTenSV();
            rowData[2] = date_format.format(sv.getNgaySinh());
            rowData[3] = sv.isGioiTinh() ? "Nam" : "Nữ";
            rowData[4] = sv.getDiaChi();
            rowData[5] = sv.getHinhAnh();
            model.addRow(rowData);
        }
    }

    public void setModel(SinhVien sv) {
        txtMasv.setText(sv.getMasv());
        txtHoten.setText(sv.getTenSV());
        txtNgaysinh.setText(date_format.format(sv.getNgaySinh()));
        boolean gt = sv.isGioiTinh();
        if (gt) {
            rdoNam.setSelected(true);
        } else {
            rdoNu.setSelected(true);
        }
        txtDiachi.setText(sv.getDiaChi());
        if (sv.getHinhAnh().equalsIgnoreCase("No img")) {
            lblHinhanh.setText(sv.getHinhAnh());
            lblHinhanh.setIcon(null);
        } else {
            lblHinhanh.setText("");
            ImageIcon imgIcon = new ImageIcon(getClass().getResource("../img/" + sv.getHinhAnh()));
            if (imgIcon != null) {
                Image img = imgIcon.getImage();
                img.getScaledInstance(lblHinhanh.getWidth(), lblHinhanh.getY(), 0);
                lblHinhanh.setIcon(imgIcon);
            } else {
                lblHinhanh.setText("Can't to find the picture");
            }
        }
    }

    public SinhVien getStudentAtPosition(int pos) {
        SinhVien sv = new SinhVien();
        sv.setMasv(tblSinhVien.getValueAt(pos, 0).toString());
        sv.setTenSV(tblSinhVien.getValueAt(pos, 1).toString());
        try {
            Date ngaySinh = date_format.parse(tblSinhVien.getValueAt(pos, 2).toString());
            sv.setNgaySinh(ngaySinh);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean gioiTinh = Boolean.parseBoolean(tblSinhVien.getValueAt(pos, 3).toString());
        sv.setGioiTinh(gioiTinh);

        sv.setDiaChi(tblSinhVien.getValueAt(pos, 4).toString());
        sv.setHinhAnh(tblSinhVien.getValueAt(pos, 5).toString());
        tblSinhVien.setRowSelectionInterval(pos, pos);
        return sv;
    }

    public void setFullnameAndEmail(String fullname, String email) {
        this.fullname = fullname;
        this.email = email;

        // Hiển thị thông tin fullname và email lên các thành phần trên form (JLabel, JTextField, ...)
        lblCB.setText("Cán Bộ: " + fullname);
        lblEmail.setText("Email: " + email);
    }

    private int getRowByValue(JTable table, Object value, int column) {
        for (int i = 0; i < table.getRowCount(); i++) {
            if (table.getValueAt(i, column).equals(value)) {
                return i;
            }
        }
        return -1; // Trả về -1 nếu không tìm thấy giá trị trong bảng
    }

    private void performSearch() {
        String maSVToFind = txtFindMaSV.getText();
        if (maSVToFind.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter ID Student");
        } else {
            int columnIndex = 0; // Đặt columnIndex là 0 nếu muốn tìm kiếm trong cột Mã SV
            int rowIndex = getRowByValue(tblSinhVien, maSVToFind, columnIndex);
            if (rowIndex != -1) {
                // Di chuyển đến vị trí tìm thấy trong bảng
                tblSinhVien.getSelectionModel().setSelectionInterval(rowIndex, rowIndex);
                tblSinhVien.scrollRectToVisible(tblSinhVien.getCellRect(rowIndex, 0, true));
            } else {
                JOptionPane.showMessageDialog(this, "Student ID not found");
            }
        }
    }

    public void UploadImg() {
        try {
            JFileChooser jfc = new JFileChooser("D:\\StudyClollege\\SOF203_Java3\\Assignment\\asmjava3\\src\\asmjava3\\img");
            int result = jfc.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = jfc.getSelectedFile();
                Image img = ImageIO.read(file);
                strHinhAnh = file.getName();
                int width = lblHinhanh.getWidth();
                int height = lblHinhanh.getHeight();
                lblHinhanh.setIcon(new ImageIcon(img.getScaledInstance(width, height, 0)));
                lblHinhanh.setText("");

                // Lưu icon mới vào biến tạm
                currentIcon = lblHinhanh.getIcon();
            } else if (result == JFileChooser.CANCEL_OPTION) {
                // Người dùng đã nhấn cancel, đặt lại icon của lblHinhanh với giá trị trước đó
                lblHinhanh.setIcon(currentIcon);
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.toString());
        }
    }

    private void updateTotalStudent() {
        int totalStudents = tblSinhVien.getRowCount();
        lblTotalStudents.setText("Total Students: " + totalStudents);
    }

    private void updateRecordPosition() {
        if (totalRows > 0 && currentRow >= 0) {
            int currentRecord = currentRow + 1;
            String recordPosition = "Record " + currentRecord + " of " + totalRows;
            lblRecord.setText(recordPosition);
        } else {
            lblRecord.setText("No records");
        }
    }

    private void setCurrentRowAndTotalRows() {
        totalRows = tblSinhVien.getRowCount();
        currentRow = tblSinhVien.getSelectedRow();
        updateRecordPosition();
    }

    public void Delete() {
        if (txtMasv.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter ID you want to delete it !");
        } else {
            if (dao.delSinhVienByID(txtMasv.getText()) > 0) {
                JOptionPane.showMessageDialog(this, "Delete Successfully");
                fillDataTABLE();
                updateTotalStudent();
            } else {
                JOptionPane.showMessageDialog(this, "Can't delete this Student or can't find ID Student !");
            }
        }
    }

    private void Update() {
        int selectedRow = tblSinhVien.getSelectedRow();
        if (selectedRow != -1) {
            if (validateUpdate()) {
                try {
                    SinhVien s = getModel();
                    if (dao.updateSinhVienByID(s) > 0) {
                        JOptionPane.showMessageDialog(this, "Update Successfully !");
                        fillDataTABLE();
                        setCurrentRowAndTotalRows();
                        // Set lại vị trí sau khi update
                        if (selectedRow < tblSinhVien.getRowCount()) {
                            tblSinhVien.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
                            tblSinhVien.scrollRectToVisible(tblSinhVien.getCellRect(selectedRow, 0, true));
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Error: " + ex.toString());
                }
            } else {
                JOptionPane.showMessageDialog(this, "All infomation need to fill !");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to update !");
        }
    }

    private void Save() {

        if (validateSave()) {
            try {
                SinhVien sv = getModel();
                if (dao.add(sv) > 0) {
                    // Đầu tiên, cập nhật dữ liệu và hiển thị lại bảng
                    fillDataTABLE();
                    updateTotalStudent();
                    // Tìm vị trí sinh viên vừa thêm vào bằng mã sinh viên
                    int rowIndex = getRowByValue(tblSinhVien, sv.getMasv(), 0);
                    if (rowIndex != -1) {
                        // Di chuyển đến vị trí sinh viên vừa thêm vào và chọn hàng đó
                        tblSinhVien.changeSelection(rowIndex, 0, false, false);
                        tblSinhVien.scrollRectToVisible(tblSinhVien.getCellRect(rowIndex, 0, true));
                    }

                    JOptionPane.showMessageDialog(this, "Save Complete !");

                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.toString());
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtMasv = new javax.swing.JTextField();
        rdoNam = new javax.swing.JRadioButton();
        rdoNu = new javax.swing.JRadioButton();
        txtHoten = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDiachi = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        txtNgaysinh = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnThem = new javax.swing.JButton();
        txtSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnLuu = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        lblHinhanh = new javax.swing.JLabel();
        btnRemovePhoto = new javax.swing.JButton();
        btnChangePhoto = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSinhVien = new javax.swing.JTable();
        btnEnd = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnPre = new javax.swing.JButton();
        btnStart = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        btnExit = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtFindMaSV = new javax.swing.JTextField();
        btnFind = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        btnLogOut = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        lblCB = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblTotalStudents = new javax.swing.JLabel();
        lblRecord = new javax.swing.JLabel();
        lblError = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Họ tên:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Địa chỉ:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Giới tính:");

        buttonGroup1.add(rdoNam);
        rdoNam.setSelected(true);
        rdoNam.setText("Nam");
        rdoNam.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        buttonGroup1.add(rdoNu);
        rdoNu.setText("Nữ");
        rdoNu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        txtDiachi.setColumns(20);
        txtDiachi.setRows(5);
        jScrollPane2.setViewportView(txtDiachi);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Ngày sinh:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Mã SV:");

        btnThem.setBackground(new java.awt.Color(102, 255, 102));
        btnThem.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThem.setForeground(new java.awt.Color(255, 255, 255));
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asmjava3/img/1491254405-plusaddmoredetail_82972.png"))); // NOI18N
        btnThem.setText("Thêm");
        btnThem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        txtSua.setBackground(new java.awt.Color(204, 204, 255));
        txtSua.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtSua.setForeground(new java.awt.Color(255, 255, 255));
        txtSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asmjava3/img/editdocument_105148.png"))); // NOI18N
        txtSua.setText("Sửa");
        txtSua.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        txtSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSuaActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(255, 51, 51));
        btnXoa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnXoa.setForeground(new java.awt.Color(255, 255, 255));
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asmjava3/img/trash_bin_icon-icons.com_67981.png"))); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnLuu.setBackground(new java.awt.Color(0, 153, 255));
        btnLuu.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLuu.setForeground(new java.awt.Color(255, 255, 255));
        btnLuu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asmjava3/img/savetheapplication_guardar_2958.png"))); // NOI18N
        btnLuu.setText("Lưu");
        btnLuu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Upload Photo", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        lblHinhanh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHinhanh.setText("Click to choose photo");
        lblHinhanh.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblHinhanh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHinhanhMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblHinhanh, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblHinhanh, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnRemovePhoto.setBackground(new java.awt.Color(255, 153, 153));
        btnRemovePhoto.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnRemovePhoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asmjava3/img/trash_bin_icon-icons.com_67981.png"))); // NOI18N
        btnRemovePhoto.setText("Remove Photo");
        btnRemovePhoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemovePhotoActionPerformed(evt);
            }
        });

        btnChangePhoto.setBackground(new java.awt.Color(255, 204, 204));
        btnChangePhoto.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnChangePhoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asmjava3/img/twocirclingarrows1_120592.png"))); // NOI18N
        btnChangePhoto.setText("Change Photo");
        btnChangePhoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangePhotoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel7)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMasv)
                            .addComponent(txtHoten)
                            .addComponent(jScrollPane2)
                            .addComponent(txtNgaysinh)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 6, Short.MAX_VALUE)
                        .addComponent(btnThem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSua, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(27, 27, 27)
                                .addComponent(rdoNam)
                                .addGap(18, 18, 18)
                                .addComponent(rdoNu))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnRemovePhoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnChangePhoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtMasv, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtHoten, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNgaysinh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(rdoNam)
                    .addComponent(rdoNu))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(btnChangePhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(btnRemovePhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSua, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblSinhVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã SV", "Họ và Tên", "Ngày sinh", "Giới tính", "Địa chỉ", "Hình ảnh"
            }
        ));
        tblSinhVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSinhVienMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSinhVien);

        btnEnd.setText(">|");
        btnEnd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEndActionPerformed(evt);
            }
        });

        btnNext.setText(">>");
        btnNext.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnPre.setText("<<");
        btnPre.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreActionPerformed(evt);
            }
        });

        btnStart.setText("|<");
        btnStart.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(266, 266, 266)
                .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 60, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnPre, javax.swing.GroupLayout.PREFERRED_SIZE, 61, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 61, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 60, Short.MAX_VALUE)
                .addGap(227, 227, 227))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnStart, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(btnEnd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("More Option"));

        btnExit.setText("Thoát");
        btnExit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        jLabel1.setText("Tìm Mã Sinh Viên");

        txtFindMaSV.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFindMaSVKeyPressed(evt);
            }
        });

        btnFind.setText("Tìm");
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFindMaSV, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFind)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExit)
                    .addComponent(jLabel1)
                    .addComponent(txtFindMaSV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFind))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(0, 195, 167));
        jPanel6.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel9.setText("Coppyright © 2023  Company All Right Resever");

        jLabel10.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Xin Chào");

        lblEmail.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblEmail.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEmail.setText("Email:");
        lblEmail.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 1, 0));

        btnLogOut.setBackground(new java.awt.Color(255, 51, 51));
        btnLogOut.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        btnLogOut.setForeground(new java.awt.Color(255, 255, 255));
        btnLogOut.setText("Đăng Xuất");
        btnLogOut.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));
        btnLogOut.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogOut.setVerifyInputWhenFocusTarget(false);
        btnLogOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogOutActionPerformed(evt);
            }
        });

        jLabel12.setBackground(new java.awt.Color(0, 195, 167));
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asmjava3/img/sms-logo1.png"))); // NOI18N
        jLabel12.setOpaque(true);
        jLabel12.setPreferredSize(new java.awt.Dimension(300, 200));

        lblCB.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblCB.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCB.setText("Cán Bộ:");
        lblCB.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 1, 0));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblCB, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(169, 169, 169))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(btnLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblCB, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(97, 97, 97)
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 123, Short.MAX_VALUE)
                .addComponent(btnLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jLabel9))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 51, 51));
        jLabel5.setText("QUẢN LÍ SINH VIÊN");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(109, 109, 109))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel6.setText("Ver 1.8 SMBL2 SD18319");

        lblTotalStudents.setText("Tổng số sinh viên:");

        lblRecord.setText("No Record");

        lblError.setText("Error: none");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblTotalStudents)
                        .addGap(81, 81, 81)
                        .addComponent(lblRecord)
                        .addGap(156, 156, 156)
                        .addComponent(lblError)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lblTotalStudents)
                    .addComponent(lblRecord)
                    .addComponent(lblError)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        int option = JOptionPane.showConfirmDialog(null, "Thao tác này sẽ xóa các dữ liệu đang được điền ?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            reset();
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        int option = JOptionPane.showConfirmDialog(null, "Thao tác này sẽ xóa điểm của Mã SV: " + txtMasv.getText() + " ?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            Delete();
            reset();
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuActionPerformed
        Save();
        setCurrentRowAndTotalRows();
        int selectedRow = tblSinhVien.getSelectedRow();
        if (selectedRow != -1 && selectedRow < tblSinhVien.getRowCount()) {
            tblSinhVien.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
            tblSinhVien.scrollRectToVisible(tblSinhVien.getCellRect(selectedRow, 0, true));
        }
    }//GEN-LAST:event_btnLuuActionPerformed

    private void tblSinhVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSinhVienMouseClicked
        int id = tblSinhVien.rowAtPoint(evt.getPoint());
        String masv = tblSinhVien.getValueAt(id, 0).toString();
        SinhVien sv = dao.getSinhVienByID(masv);
        setModel(sv);
        setCurrentRowAndTotalRows();
    }//GEN-LAST:event_tblSinhVienMouseClicked

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        index = 0;
        SinhVien s = getStudentAtPosition(index);
        setModel(s);
        setCurrentRowAndTotalRows();
    }//GEN-LAST:event_btnStartActionPerformed

    private void btnPreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreActionPerformed
        index--;
        if (index <= 0) {
            index = 0;
        }
        SinhVien s = getStudentAtPosition(index);
        setModel(s);
        setCurrentRowAndTotalRows();
    }//GEN-LAST:event_btnPreActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        index++;
        if (index >= dao.getAllSinhVien().size() - 1) {
            index = dao.getAllSinhVien().size() - 1;
        }
        SinhVien s = getStudentAtPosition(index);
        setModel(s);
        setCurrentRowAndTotalRows();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEndActionPerformed
        index = dao.getAllSinhVien().size() - 1;
        SinhVien s = getStudentAtPosition(index);
        setModel(s);
        setCurrentRowAndTotalRows();
    }//GEN-LAST:event_btnEndActionPerformed

    private void txtSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSuaActionPerformed
        Update();
    }//GEN-LAST:event_txtSuaActionPerformed

    private void lblHinhanhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHinhanhMouseClicked
        UploadImg();
    }//GEN-LAST:event_lblHinhanhMouseClicked

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnLogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogOutActionPerformed
        Session session = Session.getInstance();
        session.clearCredentials();
        LoginDialog LD = new LoginDialog();
        this.dispose();
        LD.setVisible(true);
    }//GEN-LAST:event_btnLogOutActionPerformed

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindActionPerformed
        performSearch();
    }//GEN-LAST:event_btnFindActionPerformed

    private void txtFindMaSVKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFindMaSVKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            // Thực hiện chức năng tìm kiếm ở đây
            performSearch();
        }
    }//GEN-LAST:event_txtFindMaSVKeyPressed

    private void btnRemovePhotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemovePhotoActionPerformed
        lblHinhanh.setIcon(null);
        lblHinhanh.setText("Click to choose Image");

        // Đặt lại biến tạm về giá trị ban đầu
        currentIcon = null;
    }//GEN-LAST:event_btnRemovePhotoActionPerformed

    private void btnChangePhotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangePhotoActionPerformed
        UploadImg();
    }//GEN-LAST:event_btnChangePhotoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SinhVienDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SinhVienDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SinhVienDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SinhVienDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SinhVienDialog().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChangePhoto;
    private javax.swing.JButton btnEnd;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnFind;
    private javax.swing.JButton btnLogOut;
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPre;
    private javax.swing.JButton btnRemovePhoto;
    private javax.swing.JButton btnStart;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblCB;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblError;
    private javax.swing.JLabel lblHinhanh;
    private javax.swing.JLabel lblRecord;
    private javax.swing.JLabel lblTotalStudents;
    private javax.swing.JRadioButton rdoNam;
    private javax.swing.JRadioButton rdoNu;
    private javax.swing.JTable tblSinhVien;
    private javax.swing.JTextArea txtDiachi;
    private javax.swing.JTextField txtFindMaSV;
    private javax.swing.JTextField txtHoten;
    private javax.swing.JTextField txtMasv;
    private javax.swing.JTextField txtNgaysinh;
    private javax.swing.JButton txtSua;
    // End of variables declaration//GEN-END:variables
}
