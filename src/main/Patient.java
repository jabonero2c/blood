package main;

import config.config;
import java.time.LocalDate;
import java.util.Scanner;

public class Patient {
    private final int userId;
    Scanner sc = new Scanner(System.in);
    config conf = new config();

    public Patient(int userId) {
        this.userId = userId;
    }
    
    public void createBloodRequest() {
        System.out.print("Enter required blood type (e.g., A+, O-): ");
        String bloodType = sc.next();
        System.out.print("Enter quantity needed (e.g., 2 units): ");
        int quantity = sc.nextInt();
        
        String date = LocalDate.now().toString(); 
        System.out.println("Date recorded: " + date); 
        
        String status = "Pending"; 

        String sql = "INSERT INTO blood_request(u_id, r_bloodtype, r_quantity, r_date, r_status) VALUES (?, ?, ?, ?, ?)";
        conf.addRecord(sql, this.userId, bloodType, quantity, date, status);
    }

    public void viewMyRequests() {
        System.out.println("\n--- My Blood Requests ---");
        String sql = "SELECT * FROM blood_request WHERE u_id = ?";
        String[] headers = {"ID", "Blood Type", "Quantity", "Date", "Status"};
        String[] columns = {"r_id", "r_bloodtype", "r_quantity", "r_date", "r_status"};

        java.util.List<java.util.Map<String, Object>> records = conf.fetchRecords(sql, this.userId);
        
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("| %-20s | %-20s | %-20s | %-20s | %-20s |\n", headers[0], headers[1], headers[2], headers[3], headers[4]);
        System.out.println("--------------------------------------------------------------------------------");
        
        for (java.util.Map<String, Object> record : records) {
            System.out.printf("| %-20s | %-20s | %-20s | %-20s | %-20s |\n",
                record.get("r_id"), record.get("r_bloodtype"), record.get("r_quantity"), record.get("r_date"), record.get("r_status"));
        }
        System.out.println("--------------------------------------------------------------------------------");
    }

    public void patientDashboard() {
        int choice;
        char res = 'Y';
        do {
            System.out.println("\nPatient Dashboard:");
            System.out.println("1. Create New Blood Request");
            System.out.println("2. View My Request Status");
            System.out.println("3. Logout");
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
                    createBloodRequest();
                    break;
                case 2:
                    viewMyRequests();
                    break;
                case 3:
                    System.out.println("Logging out from Patient Dashboard...");
                    res = 'N';
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

            if (choice != 3) {
                System.out.print("Do you want to continue? (Y/N): ");
                res = sc.next().charAt(0);
            }

        } while (res == 'Y' || res == 'y');
    }
}