package main;

import config.config;
import java.util.Scanner;

public class admin {
    Scanner sc = new Scanner (System.in);
    config conf = new config();
    
    public void approveUser() {
        System.out.print("Enter ID to Approve: ");
        int id = sc.nextInt();

        String sql = "UPDATE tbl_users SET u_status = ? WHERE u_id = ?";
        conf.updateRecord(sql, "Approved", id);
    }

    public void deleteUser() {
        System.out.print("Enter ID to delete: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM tbl_users WHERE u_id = ?";
        conf.deleteRecord(sql, id);
    }

    public void viewUsers() {
        String sql = "SELECT * FROM tbl_users";
        String[] userHeaders = {"ID", "Name", "Email", "Role", "Status"};
        String[] userColumns = {"u_id", "u_name", "u_email", "u_role", "u_status"};
        conf.viewRecords(sql, userHeaders, userColumns);
    }
    
    public void viewAllDonations() {
        System.out.println("\n--- All Donation Records ---");
        String sql = "SELECT * FROM donation_records";
        String[] headers = {"ID", "User ID", "Blood Type", "Quantity", "Date", "Notes"};
        String[] columns = {"d_id", "u_id", "d_bloodtype", "d_quantity", "d_date", "d_notes"};
        conf.viewRecords(sql, headers, columns);
    }
    
    public void viewAllRequests() {
        System.out.println("\n--- All Patient Blood Requests ---");
        String sql = "SELECT * FROM blood_request";
        String[] headers = {"ID", "User ID", "Blood Type", "Quantity", "Date", "Status"};
        String[] columns = {"r_id", "u_id", "r_bloodtype", "r_quantity", "r_date", "r_status"};
        conf.viewRecords(sql, headers, columns);
    }
    
    public void processRequest() {
        viewAllRequests();
        
        System.out.print("Enter Request ID to process: ");
        int requestId = sc.nextInt();
        sc.nextLine();
        
        System.out.println("Set new status (e.g., Fulfilled, In Progress, Rejected):");
        System.out.print("Enter new status: ");
        String newStatus = sc.nextLine();
        
        String sql = "UPDATE blood_request SET r_status = ? WHERE r_id = ?";
        conf.updateRecord(sql, newStatus, requestId);
    }

    public void adminDashboard() {
    int choice;
    char res = 'Y';
    do {
        System.out.println("\nAdmin Menu:");
        System.out.println("1. View Users");
        System.out.println("2. Approve User");
        System.out.println("3. Delete User");
        System.out.println("4. View All Donations");
        System.out.println("5. View All Requests");
        System.out.println("6. Fulfill/Process Request");
        System.out.println("7. Logout");
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
                viewUsers();
                break;
            case 2:
                viewUsers();
                approveUser();
                break;
            case 3:
                deleteUser();
                break;
            case 4:
                viewAllDonations();
                break;
            case 5:
                viewAllRequests();
                break;
            case 6:
                processRequest();
                break;
            case 7:
                System.out.println("Logging out...");
                res = 'N'; 
                break;
            default:
                System.out.println("Invalid option. Please try again.");
            }
            
            if (choice != 7) {
                System.out.print("Do you want to continue? (Y/N): ");
                res = sc.next().charAt(0);
            }
            
        } while (res == 'Y' || res == 'y');
    }
    
}