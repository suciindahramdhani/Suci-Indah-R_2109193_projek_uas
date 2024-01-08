package textbasedgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;


public class TextBasedGame {
    private static Timer timer;
    public static void main(String[] args) {
        
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Masukkan nama pemain: ");
            String playerName = scanner.nextLine();
            System.out.print("Masukkan nama musuh: ");
            String enemyName = scanner.nextLine();

            Player player;
            player = new Player(playerName, 100);
            Location currentLocation = Location.getStartLocation();

            Enemy enemy;
            enemy = new Enemy(enemyName, 100);

            Inventory playerInventory;
            playerInventory = new Inventory();

            System.out.println("Selamat datang di Petualangan Teks!");
            System.out.println("Ketik 'instruksi' untuk melihat perintah-perintah yang tersedia.");

            timer = new Timer();
            OUTER:
            while (true) {
                System.out.println("Kamu berada di: " + currentLocation.getDescription());
                System.out.print("Apa yang ingin kamu lakukan? > ");
                String input = scanner.nextLine().toLowerCase();

                timer.cancel(); // Timer dibatalkan setiap input baru

                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("Waktu habis! Kamu terlalu lama memilih.");
                        // Lakukan tindakan karena timeout
                    }
                }, 10000); // Waktu timeout dalam milidetik (contoh: 10 detik)
                
                switch (input) {
                    case "instruksi" -> // Tampilkan daftar perintah yang tersedia
                        System.out.println("Perintah yang tersedia: go, attack, status, quit");
                    case "go" -> {
                        // Lakukan perintah untuk berpindah lokasi
                        System.out.println("Kamu berjalan ke...");
                        currentLocation = currentLocation.move();
                    }
                    case "attack" -> {
                        System.out.println("Kamu menyerang musuh!");
                        int playerDamage = 20;
                        enemy.setHealth(enemy.getHealth() - playerDamage); // Kurangi kesehatan musuh
                        System.out.println("Kesehatan musuh: " + enemy.getHealth());

                        if (enemy.getHealth() <= 0) {
                            System.out.println(enemyName + " telah mati!");
                        }
                    }
                    case "status" -> // Tampilkan status pemain
                        System.out.println("Kesehatan: " + player.getHealth());
                    case "inventory" -> {
                        playerInventory.displayInventory();
                        continue; // Kembali ke input setelah menampilkan inventaris
                    }
                    case "quit" -> {
                       System.out.println("Terima kasih telah bermain!");
                       timer.cancel(); // Batalkan timer sebelum keluar dari permainan
                       break OUTER;
                   }
                            default -> System.out.println("Perintah tidak valid. Ketik 'instruksi' untuk bantuan.");
                }
            }
        }
    }
    
public static class Inventory {
    private List<String> items;

    public Inventory() {
        this.items = new ArrayList<>();
    }

    public void addItem(String item) {
        items.add(item);
    }

    public void displayInventory() {
        if (items.isEmpty()) {
            System.out.println("Inventaris kosong.");
        } else {
            System.out.println("Inventaris pemain:");
            for (String item : items) {
                System.out.println("- " + item);
            }
        }
    }
}

public static class Player {
    private String name;
    private int health;

    public Player(String name, int health) {
        this.name = name;
        this.health = health;
    }

    public Player(String name) {
        this.name = name;
        this.health = 100; // Kesehatan default
    }

    public Player() {
        this.name = "Pemain Baru"; // Nama default
        this.health = 100; // Kesehatan default
    }

    public int getHealth() {
        return health;
    }

    // Metode untuk mengatur kesehatan
    public void setHealth(int health) {
        this.health = health;
    }
}

public static class Enemy {
    private String name;
    private int health;

    // Constructor dengan nama dan kesehatan musuh
    public Enemy(String name, int health) {
        this.name = name;
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}


// Kelas untuk merepresentasikan lokasi dalam permainan
public static class Location {
    private final String description;

    public Location(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // Metode untuk pindah ke lokasi lain
public Location move() {
        return switch (this.getDescription()) {
            case "Di Area Hutan" -> new Location("Di Hutan Gelap");
            case "Di Hutan Gelap" -> new Location("Di Gua Misterius");
            case "Di Gua Misterius" -> new Location("Di Pinggir Danau");
            case "Di Pinggir Danau" -> new Location("Di Dekat Jurang");   
            case "Di Dekat Jurang" -> new Location("Di Bawah Pohon");   
            case "Di Bawah Pohon" -> new Location("Di Lahan Kosong");   
            case "Di Lahan Kosong" -> new Location("Di Halaman Kastil");              
            default -> Location.getStartLocation();
        }; 
}
    public static Location getStartLocation() {
        return new Location("Di Area Hutan");
    }
}
}
