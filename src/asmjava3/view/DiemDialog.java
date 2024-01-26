/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package asmjava3.view;

import asmjava3.view.model.Grade;
import asmjava3.view.model.GradeDAO;
import asmjava3.view.model.Session;
import asmjava3.view.model.SinhVien;
import asmjava3.view.model.SinhVienDAO;
import asmjava3.view.model.userDAO;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class DiemDialog extends javax.swing.JFrame {

    SinhVienDAO svDAO = new SinhVienDAO();
    GradeDAO dDAO = new GradeDAO();
    int index = 0;
    private String fullname;
    private String email;
    private int currentRow = -1;
    private int totalRows = -1;
    private Icon currentIcon;
    int selectedRow = -1;

    public DiemDialog() {
        initComponents();
        setLocationRelativeTo(null);
        txtMasv.setEditable(false);
        txtHoten.setEditable(false);
        lblTBC.setText("0.0");
        fillDataTABLE();
        updateTotalStudent();
        String iconPath = "D://StudyClollege//SOF203_Java3//Assignment//asmjava3//src//asmjava3//img//sms-logo.png";
        Image iconImage = new ImageIcon(iconPath).getImage();
        setIconImage(iconImage);
        setTitle("Quản lí Điểm");
    }

    public void fillDataTABLE() {
        DefaultTableModel model = (DefaultTableModel) tblDiem.getModel();
        model.setRowCount(0);
        for (Grade g : dDAO.getAllGrade()) {
            Object rowData[] = new Object[7];
            rowData[0] = g.getSv().getMasv();
            rowData[1] = g.getSv().getTenSV();
            rowData[2] = g.getAnhVan();
            rowData[3] = g.getTinHoc();
            rowData[4] = g.getGdtc();
            String tb = String.format("%.2f", g.getTBC()).replace(',', '.');
            rowData[5] = tb;
            rowData[6] = g.getXepLoai();
            model.addRow(rowData);
        }
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        sorter.setSortable(0, true);
        sorter.setSortKeys(List.of(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
        tblDiem.setRowSorter(sorter);
    }

    public boolean validateForm() {
        String masv = txtMasv.getText();
        String hoten = txtHoten.getText();
        String anhVanStr = txtAnhVan.getText();
        String gdTcStr = txtGDTC.getText();
        String tinHocStr = txtTinHoc.getText();

        if (masv.isEmpty() || hoten.isEmpty() || anhVanStr.isEmpty() || gdTcStr.isEmpty() || tinHocStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all information !");
            return false;
        }
        try {
            double anhVan = Double.parseDouble(anhVanStr);
            double gdTc = Double.parseDouble(gdTcStr);
            double tinHoc = Double.parseDouble(tinHocStr);

            if (anhVan < 0 || anhVan > 10 || gdTc < 0 || gdTc > 10 || tinHoc < 0 || tinHoc > 10) {
                JOptionPane.showMessageDialog(this, "Please enter valid grade (0 - 10) for Anh Van, GDTC, and Tin Hoc !");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for Anh Van, GDTC, and Tin Hoc !");
            return false;
        }
        return true;
    }

    public Grade getModel() {
        Grade g = new Grade();
        g.setId(0);
        SinhVien sv = new SinhVien(txtMasv.getText(), txtHoten.getText());
        g.setSv(sv);
        g.setAnhVan(Double.parseDouble(txtAnhVan.getText()));
        g.setTinHoc(Double.parseDouble(txtTinHoc.getText()));
        g.setGdtc(Double.parseDouble(txtGDTC.getText()));
        return g;
    }

    public void setModel(Grade g) {
        txtAnhVan.setText(String.valueOf(g.getAnhVan()));
        txtTinHoc.setText(String.valueOf(g.getTinHoc()));
        txtGDTC.setText(String.valueOf(g.getGdtc()));
        txtMasv.setText(g.getSv().getMasv());
        txtHoten.setText(g.getSv().getTenSV());
        String tb = String.format("%.2f", g.getTBC());
        lblTBC.setText(tb);
    }

    public Grade getGradeAtPosition(int pos) {
        Grade g = new Grade();
        SinhVien sv = new SinhVien();
        sv.setMasv(tblDiem.getValueAt(pos, 0).toString());
        sv.setTenSV(tblDiem.getValueAt(pos, 1).toString());
        g.setSv(sv);
        g.setAnhVan(Double.parseDouble(tblDiem.getValueAt(pos, 2).toString()));
        g.setTinHoc(Double.parseDouble(tblDiem.getValueAt(pos, 3).toString()));
        g.setGdtc(Double.parseDouble(tblDiem.getValueAt(pos, 4).toString()));
        tblDiem.setRowSelectionInterval(pos, pos);
        return g;
    }

    public void clearForm() {
        txtFindMaSV.setText("");
        txtMasv.setText("");
        txtHoten.setText("");
        txtAnhVan.setText("");
        txtTinHoc.setText("");
        txtGDTC.setText("");
        lblTBC.setText("0.00");
    }

    public void setFullnameAndEmail(String fullname, String email) {
        this.fullname = fullname;
        this.email = email;

        // Hiển thị thông tin fullname và email lên các thành phần trên form (JLabel, JTextField, ...)
        lblGV.setText("Giảng Viên: " + fullname);
        lblEmail.setText("Email: " + email);
    }

    private void Save() {
        if (validateForm()) {
            Grade g = getModel();
            if (dDAO.getOneGradeByMaSV(g.getSv().getMasv()) != null) {
                JOptionPane.showMessageDialog(this, "ID Student is already !");
                return;
            } else {
                if (dDAO.add(g) > 0) {
                    fillDataTABLE();
                    updateTBC();
                    clearStatus();
                    JOptionPane.showMessageDialog(this, "Save Successfully !");
                    updateTotalStudent();

                    // Tìm vị trí sinh viên vừa thêm vào bằng mã sinh viên
                    int rowIndex = findRowIndexByMaSV(g.getSv().getMasv());
                    if (rowIndex != -1) {
                        // Di chuyển đến vị trí sinh viên vừa thêm vào và chọn hàng đó
                        tblDiem.setRowSelectionInterval(rowIndex, rowIndex);
                        tblDiem.scrollRectToVisible(tblDiem.getCellRect(rowIndex, 0, true));
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please fill all of information !");
                }
            }
        }
    }

    private int findRowIndexByMaSV(String maSV) {
        for (int rowIndex = 0; rowIndex < tblDiem.getRowCount(); rowIndex++) {
            String maSVInTable = tblDiem.getValueAt(rowIndex, 0).toString();
            if (maSV.equals(maSVInTable)) {
                return rowIndex;
            }
        }
        return -1; // Không tìm thấy
    }

    public void clearStatus() {
        lblStatus.setText("Status: none");
    }

    private void updateTBC() {
        String masvToFind = txtMasv.getText();
        Grade grade = dDAO.getOneGradeByMaSV(masvToFind);

        if (grade != null) {
            double tbc = (grade.getAnhVan() + grade.getTinHoc() + grade.getGdtc()) / 3;
            String formattedTBC = String.format("%.2f", tbc);
            lblTBC.setText(formattedTBC);
        } else {
            lblTBC.setText("0.0");
        }
    }

    private void updateTotalStudent() {
        int totalStudents = tblDiem.getRowCount();
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
        totalRows = tblDiem.getRowCount();
        currentRow = tblDiem.getSelectedRow();
        updateRecordPosition();
    }

    private int getRowByValue(JTable table, Object value, int column) {
        for (int i = 0; i < table.getRowCount(); i++) {
            if (table.getValueAt(i, column).equals(value)) {
                return i;
            }
        }
        return -1; // Trả về -1 nếu không tìm thấy giá trị trong bảng
    }

    public void FindMaSV() {
        if (txtFindMaSV.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter ID Student");
        } else {
            String maSVToFind = txtFindMaSV.getText();
            int columnIndex = 0;
            int rowIndex = getRowByValue(tblDiem, maSVToFind, columnIndex);
            setCurrentRowAndTotalRows();

            // Tìm kiếm trong bảng tblDiem
            if (rowIndex != -1) {
                // Di chuyển đến vị trí tìm thấy trong bảng tblSinhVien
                tblDiem.getSelectionModel().setSelectionInterval(rowIndex, rowIndex);
                tblDiem.scrollRectToVisible(tblDiem.getCellRect(rowIndex, 0, true));
                SinhVien sv = svDAO.getSinhVienByID(maSVToFind);
                if (sv != null) {
                    Grade d = dDAO.getOneGradeByMaSV(sv.getMasv());
                    setCurrentRowAndTotalRows();
                    if (d != null) {
                        setModel(d);
                    } else {
                        // Nếu không tìm thấy dữ liệu điểm, thì reset các trường dữ liệu
                        txtMasv.setText(sv.getMasv());
                        txtHoten.setText(sv.getTenSV());
                        txtAnhVan.setText("");
                        txtGDTC.setText("");
                        txtTinHoc.setText("");
                        lblTBC.setText("0.0");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Student ID not found !");
                }
            } else {
                lblStatus.setText("Error: " + txtFindMaSV.getText() + " not found on this table !");
                // Nếu không tìm thấy mã sinh viên trong bảng tblDiem, thực hiện tìm kiếm trong database
                SinhVien sv = svDAO.getSinhVienByID(maSVToFind);
                if (sv != null) {
                    Grade d = dDAO.getOneGradeByMaSV(sv.getMasv());
                    setCurrentRowAndTotalRows();
                    if (d != null) {
                        setModel(d);
                    } else {
                        // Nếu không tìm thấy dữ liệu điểm, thì reset các trường dữ liệu
                        txtMasv.setText(sv.getMasv());
                        txtHoten.setText(sv.getTenSV());
                        txtAnhVan.setText("");
                        txtGDTC.setText("");
                        txtTinHoc.setText("");
                        lblTBC.setText("0.0");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Student ID not found !");
                }
            }
        }
    }

    public void Update() {
        if (validateForm()) {
            Grade d = getModel();
            if (dDAO.updateGrade(d) > 0) {
                JOptionPane.showMessageDialog(this, "Update Successfully !");
                clearStatus();
                fillDataTABLE();
                updateTBC();
                int rowIndex = getRowByValue(tblDiem, d.getSv().getMasv(), 0);
                if (rowIndex != -1) {
                    tblDiem.getSelectionModel().setSelectionInterval(rowIndex, rowIndex);
                    tblDiem.scrollRectToVisible(tblDiem.getCellRect(rowIndex, 0, true));
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill all of information !");
            }
        }
    }

    public void Delete() {
        if (validateForm()) {
            if (dDAO.delGrade(txtMasv.getText()) > 0) {
                JOptionPane.showMessageDialog(this, "Delete Successfully !");
                clearStatus();
                fillDataTABLE();
                updateTotalStudent();
            } else {
                JOptionPane.showMessageDialog(this, "Please fill all of infomation !");
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtMasv = new javax.swing.JTextField();
        txtHoten = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtAnhVan = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnThem = new javax.swing.JButton();
        btnLuu = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        txtXoa = new javax.swing.JButton();
        txtTinHoc = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtGDTC = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblTBC = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtFindMaSV = new javax.swing.JTextField();
        btnTimKiem = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDiem = new javax.swing.JTable();
        btnEnd = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnPre = new javax.swing.JButton();
        btnStart = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        btnExit = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        btnLogOut = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        lblGV = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblTotalStudents = new javax.swing.JLabel();
        lblRecord = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 51, 51));
        jLabel1.setText("QUẢN LÍ ĐIỂM");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(140, 140, 140))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Họ tên:");

        txtMasv.setFocusable(false);
        txtMasv.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtMasvMouseClicked(evt);
            }
        });

        txtHoten.setFocusable(false);
        txtHoten.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtHotenMouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Anh Văn:");

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

        btnSua.setBackground(new java.awt.Color(204, 204, 255));
        btnSua.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSua.setForeground(new java.awt.Color(255, 255, 255));
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asmjava3/img/editdocument_105148.png"))); // NOI18N
        btnSua.setText("Sửa");
        btnSua.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        txtXoa.setBackground(new java.awt.Color(255, 51, 51));
        txtXoa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtXoa.setForeground(new java.awt.Color(255, 255, 255));
        txtXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asmjava3/img/trash_bin_icon-icons.com_67981.png"))); // NOI18N
        txtXoa.setText("Xóa");
        txtXoa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        txtXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtXoaActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Tin Học:");

        txtGDTC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGDTCKeyPressed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("GDTC:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("Điểm Trung Bình:");

        lblTBC.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblTBC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTBC.setText("0.0");

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.gray, null));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Mã Sinh Viên:");

        txtFindMaSV.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFindMaSVKeyPressed(evt);
            }
        });

        btnTimKiem.setBackground(new java.awt.Color(204, 204, 204));
        btnTimKiem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asmjava3/img/seo-social-web-network-internet_340_icon-icons.com_61497.png"))); // NOI18N
        btnTimKiem.setText("Find");
        btnTimKiem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTimKiem.setFocusable(false);
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFindMaSV)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTimKiem)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtFindMaSV, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .addComponent(btnTimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnThem)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 3, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtMasv, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                                    .addComponent(txtTinHoc)
                                    .addComponent(txtAnhVan)
                                    .addComponent(txtHoten)
                                    .addComponent(txtGDTC))))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblTBC, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(148, 148, 148))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(161, 161, 161))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtMasv, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtHoten, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAnhVan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTinHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGDTC, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(lblTBC, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 144, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblDiem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã SV", "Tên SV", "Anh Văn", "Tin Học", "GDTC", "TBC", "Xếp Loại"
            }
        ));
        tblDiem.setToolTipText("");
        tblDiem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblDiem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDiemMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDiem);

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
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(248, 248, 248)
                .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 60, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnPre, javax.swing.GroupLayout.PREFERRED_SIZE, 61, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 61, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 60, Short.MAX_VALUE)
                .addGap(195, 195, 195))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnStart, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(btnNext, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPre, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEnd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(655, Short.MAX_VALUE)
                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnExit))
        );

        jPanel6.setBackground(new java.awt.Color(0, 195, 167));
        jPanel6.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel6.setText("Coppyright © 2023  Company All Right Resever");

        jLabel8.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Xin Chào");

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

        lblGV.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblGV.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGV.setText("Giảng Viên: ");
        lblGV.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 1, 0));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblGV, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel6)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))))
                .addGap(0, 6, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(jLabel8))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(btnLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblGV, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 159, Short.MAX_VALUE)
                .addComponent(btnLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6))
        );

        jLabel11.setText("Ver 2.6 SMBL2 SD18319");

        lblTotalStudents.setText("Tổng số sinh viên:");

        lblRecord.setText("No Record");

        lblStatus.setText("Status: none");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblTotalStudents)
                        .addGap(93, 93, 93)
                        .addComponent(lblRecord)
                        .addGap(87, 87, 87)
                        .addComponent(lblStatus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel11))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(lblTotalStudents)
                    .addComponent(lblRecord)
                    .addComponent(lblStatus)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        int option = JOptionPane.showConfirmDialog(null, "Thao tác này sẽ xóa các dữ liệu đang được điền ?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            // Thực hiện chức năng clearForm() nếu người dùng chọn "Yes"
            clearForm();
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void txtXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtXoaActionPerformed
        int option = JOptionPane.showConfirmDialog(null, "Thao tác này sẽ xóa điểm của Mã SV: " + txtMasv.getText() + " ?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            // Thực hiện chức năng clearForm() nếu người dùng chọn "Yes"
            Delete();
            clearForm();
        }

    }//GEN-LAST:event_txtXoaActionPerformed

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuActionPerformed
        Save();
        setCurrentRowAndTotalRows();
        if (selectedRow != -1 && selectedRow < tblDiem.getRowCount()) {
            tblDiem.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
            tblDiem.scrollRectToVisible(tblDiem.getCellRect(selectedRow, 0, true));
        }
    }//GEN-LAST:event_btnLuuActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnExitActionPerformed

    private void tblDiemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDiemMouseClicked
        int index = tblDiem.rowAtPoint(evt.getPoint());
        Grade g = getGradeAtPosition(index);
        setModel(g);
        setCurrentRowAndTotalRows();
    }//GEN-LAST:event_tblDiemMouseClicked

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        index = 0;
        Grade g = getGradeAtPosition(index);
        setModel(g);
        setCurrentRowAndTotalRows();
    }//GEN-LAST:event_btnStartActionPerformed

    private void btnPreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreActionPerformed
        index--;
        if (index <= 0) {
            index = 0;
        }
        Grade g = getGradeAtPosition(index);
        setModel(g);
        setCurrentRowAndTotalRows();
    }//GEN-LAST:event_btnPreActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        index++;
        if (index >= dDAO.getAllGrade().size() - 1) {
            index = dDAO.getAllGrade().size() - 1;
        }
        Grade d = getGradeAtPosition(index);
        setModel(d);
        setCurrentRowAndTotalRows();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEndActionPerformed
        index = dDAO.getAllGrade().size() - 1;
        Grade g = getGradeAtPosition(index);
        setModel(g);
        setCurrentRowAndTotalRows();
    }//GEN-LAST:event_btnEndActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        Update();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        FindMaSV();

    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void btnLogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogOutActionPerformed
        Session session = Session.getInstance();
        session.clearCredentials();
        LoginDialog LD = new LoginDialog();
        this.dispose();
        LD.setVisible(true);
    }//GEN-LAST:event_btnLogOutActionPerformed

    private void txtGDTCKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGDTCKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            // Thực hiện chức năng tìm kiếm ở đây
            Save();
        }
    }//GEN-LAST:event_txtGDTCKeyPressed

    private void txtFindMaSVKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFindMaSVKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            // Thực hiện chức năng tìm kiếm ở đây
            FindMaSV();
        }
    }//GEN-LAST:event_txtFindMaSVKeyPressed

    private void txtMasvMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMasvMouseClicked
        lblStatus.setText("Status: you can't edit or add on thís text field");
    }//GEN-LAST:event_txtMasvMouseClicked

    private void txtHotenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtHotenMouseClicked
        lblStatus.setText("Status: you can't edit or add on thís text field");
    }//GEN-LAST:event_txtHotenMouseClicked

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
            java.util.logging.Logger.getLogger(DiemDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DiemDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DiemDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DiemDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DiemDialog().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEnd;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnLogOut;
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPre;
    private javax.swing.JButton btnStart;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblGV;
    private javax.swing.JLabel lblRecord;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTBC;
    private javax.swing.JLabel lblTotalStudents;
    private javax.swing.JTable tblDiem;
    private javax.swing.JTextField txtAnhVan;
    private javax.swing.JTextField txtFindMaSV;
    private javax.swing.JTextField txtGDTC;
    private javax.swing.JTextField txtHoten;
    private javax.swing.JTextField txtMasv;
    private javax.swing.JTextField txtTinHoc;
    private javax.swing.JButton txtXoa;
    // End of variables declaration//GEN-END:variables

    private void getTCB() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
