package main;

import config.config;
import java.time.LocalDate;
import java.util.Scanner;

public class Donor {
    private final int userId;
    Scanner sc = new Scanner(System.in);
    config conf = new config();

    public Donor(int userId) {
        this.userId = userId;
    }
    
    public void addDonationRecord() {
        System.out.print("Enter blood type (e.g., A+, O-): ");
        String bloodType = sc.next();
        System.out.print("Enter quantity (e.g., 1 for one unit): ");
        int quantity = sc.nextInt();
        
        String date = LocalDate.now().toString(); 
        System.out.println("Date recorded: " + date); 
        
        sc.nextLine(); 
        System.out.print("Enter notes (optional): ");
        String notes = sc.nextLine();

        String sql = "INSERT INTO donation_records(u_id, d_bloodtype, d_quantity, d_date, d_notes) VALUES (?, ?, ?, ?, ?)";
        conf.addRecord(sql, this.userId, bloodType, quantity, date, notes);
    }

    public void viewMyDonations() {
        System.out.println("\n--- My Donation Records ---");
        String sql = "SELECT * FROM donation_records WHERE u_id = ?";
        String[] headers = {"ID", "Blood Type", "Quantity", "Date", "Notes"};
        String[] columns = {"d_id", "d_bloodtype", "d_quantity", "d_date", "d_notes"};
        
        java.util.List<java.util.Map<String, Object>> records = conf.fetchRecords(sql, this.userId);
        
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("| %-20s | %-20s | %-20s | %-20s | %-20s |\n", headers[0], headers[1], headers[2], headers[3], headers[4]);
        System.out.println("--------------------------------------------------------------------------------");
        
        for (java.util.Map<String, Object> record : records) {
            System.out.printf("| %-20s | %-20s | %-20s | %-20s | %-20s |\n",
                record.get("d_id"), record.get("d_bloodtype"), record.get("d_quantity"), record.get("d_date"), record.get("d_notes"));
        }
        System.out.println("--------------------------------------------------------------------------------");
    }

    public void donorDashboard() {
        int choice;
        char res = 'Y';
        do {
            System.out.println("\nDonor Dashboard:");
            System.out.println("1. Add Donation Record");
            System.out.println("2. View My Donation History");
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
                    addDonationRecord();
                    break;
                case 2:
                    viewMyDonations();
                    break;
                case 3:
                    System.out.println("Logging out from Donor Dashboard...");
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