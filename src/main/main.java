package main;

import config.config;
import java.util.Scanner;

public class main {

    config conf = new config();
    Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        main system = new main();
        system.run();
    }

    public void run() {

        int choice;
        do {
            System.out.println("Welcome to the Blood Donation System!");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
            } else {
                sc.next();
                choice = 0;
                continue;
            }

            switch (choice) {
                case 1:
                    loginUser();
                    break;
                case 2:
                    registerUser();
                    break;
                case 3:
                    System.out.println("Thanks for using SaveABlood!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (choice != 3);
    }

    public void loginUser() {

        System.out.print("Enter Email: ");
        String em = sc.next();
        System.out.print("Enter Password: ");
        String pw = sc.next();

        String hp = conf.hashPassword(pw);
        
        String qry = "SELECT * FROM tbl_users WHERE u_email = ? AND u_pass = ?";
        java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, em, hp);

        if (result.isEmpty()) {
            System.out.println("INVALID CREDENTIALS");
            return;
        } else {
            java.util.Map<String, Object> user = result.get(0);
            String stat = user.get("u_status").toString();
            String role = user.get("u_role").toString();
            int u_id = (int) user.get("u_id"); 

            if (stat.equals("Pending")) {
                System.out.println("Account is Pending, Contact Admin for Approval!");
                return;
            } else {
                System.out.println("LOGIN SUCCESS!");
                if (role.equals("Admin")) {
                    admin admin = new admin();
                    admin.adminDashboard();
                } else if (role.equals("Donor")) { 
                    Donor donor = new Donor(u_id);
                    donor.donorDashboard();
                } else if (role.equals("Patient")) { 
                    Patient patient = new Patient(u_id);
                    patient.patientDashboard();
                }
            }
        }
    }

    public void registerUser() {
        System.out.print("Enter user Role (1 for Admin, 2 for Donor, 3 for Patient): ");
        int role = sc.nextInt();
        while (role > 3 || role < 1) {
            System.out.print("Invalid Role! Choose between 1 to 3 only: ");
            role = sc.nextInt();
        }

        String stat, url = "";
        if (role == 1) {
            url = "Admin";
    
            String checkAdmin = "SELECT * FROM tbl_users WHERE u_role = ?";
            java.util.List<java.util.Map<String, Object>> adminCheck = conf.fetchRecords(checkAdmin, "Admin");

            if (adminCheck.isEmpty()) {
                stat = "Approved"; 
            } else {
                stat = "Pending";    
            }

        } else if (role == 2) {
            url = "Donor";
            stat = "Pending";
        } else {
            url = "Patient";
            stat = "Pending";
        }

        System.out.print("Enter user name: ");
        String name = sc.next();
        System.out.print("Enter user email: ");
        String email = sc.next();

        while (true) {
            String qry = "SELECT * FROM tbl_users WHERE u_email = ?";
            java.util.List<java.util.Map<String, Object>> result = conf.fetchRecords(qry, email);

            if (result.isEmpty()) {
                break;
            } else {
                System.out.print("Email already exists, Enter another Email: ");
                email = sc.next();
            }
        }

        System.out.print("Enter Password: ");
        String pass = sc.next();
        
        String hpd = conf.hashPassword(pass);

        String sql = "INSERT INTO tbl_users(u_name, u_email, u_role, u_status, u_pass) VALUES (?, ?, ?, ?, ?)";
        conf.addRecord(sql, name, email, url, stat, hpd);
    }

}