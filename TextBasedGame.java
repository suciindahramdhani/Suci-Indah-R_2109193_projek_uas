//Nama: Suci Indah Ramdhani. NIM: 2109193//

package textbasedgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import textbasedgame.TextBasedGame.GameEventListener;


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

            
            var enemy = new Enemy(enemyName, 100, new GameEventListener() {
            @Override
            public void onEnemyDefeated(String defeatedEnemyName) {
                System.out.println(defeatedEnemyName + " telah mati!");
            }

            @Override
            public void onGameFinish() {
                System.out.println("Permainan telah selesai!");
            }
        });
        
        BossEnemy boss = new BossEnemy("Raja Musuh", 250, new GameEventListener() {
            @Override
            public void onEnemyDefeated(String defeatedEnemyName) {
                System.out.println(defeatedEnemyName + " telah mati!");
            }

            @Override
            public void onGameFinish() {
                System.out.println("Permainan telah selesai!");
            }
        });
            Inventory playerInventory;
            playerInventory = new Inventory();

            System.out.println(" ");
            System.out.println(" ");
            System.out.println("================= SELAMAT DATANG DI PERMAINAN PETUALANGAN TEKS! =====================");
            System.out.println("========= Ketik 'instruksi' untuk melihat perintah-perintah yang tersedia. ==========");
            System.out.println(" ");
            System.out.println(" ");

            timer = new Timer();
            OUTER:
            while (true) {
                System.out.println("Kamu kini berada di: " + currentLocation.getDescription());
                System.out.print("Apa yang ingin kamu lakukan? > ");
                String input;
                input = scanner.nextLine();

                timer.cancel();

                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("Waktu habis! Kamu terlalu lama memilih.");
                    }
                }, 10000); 
                
                switch (input) {
                    case "instruksi" ->{ // Tampilkan daftar perintah yang tersedia
                        System.out.println(" ");
                        System.out.println("Pilih antara 1-5");
                        System.out.println("Perintah yang tersedia: ");
                        System.out.println("1. Go");
                        System.out.println("2. Attack");
                        System.out.println("3. Status");
                        System.out.println("4. Inventary");
                        System.out.println("5. Quit (Keluar Game)");
                        System.out.println(" ");
                    }
                    case "1" -> {
                        System.out.println("1. Go");
                        System.out.println("Kamu berjalan ke...");
                        currentLocation = currentLocation.move();
                    }
                    case "2" -> {
                        if (enemy.getHealth() <= 0) {
                            System.out.println("Musuh sudah mati, tidak bisa menyerang lagi!");
                            continue; // Kembali ke input setelah pesan ditampilkan
                        }

                        System.out.println("Kamu menyerang musuh!");
                        int playerDamage = 20;

                        if (enemy.getHealth() > 0) {
                            enemy.setHealth(enemy.getHealth() - playerDamage); // Kurangi kesehatan musuh
                        }

                        if (enemy.getHealth() <= 0) {
                            System.out.println(enemyName + " telah mati!");
                        } else {
                            System.out.println("Kesehatan musuh: " + enemy.getHealth());
                        }
                    }
                    case "3" -> { // Tampilkan status pemain
                        System.out.println("3. Status");
                        System.out.println("Kesehatan: " + player.getHealth());
                    }
                    case "4" -> {
                        System.out.println("4. Inventaris");
                        System.out.println("Pilihan inventaris:");
                        System.out.println("1. Tampilkan inventaris");
                        System.out.println("2. Tambah item");
                        System.out.println("3. Hapus item");
                        System.out.println("4. Bersihkan inventaris");
                        System.out.println("Pilih aksi (1-4): ");
                        int inventoryAction = Integer.parseInt(scanner.nextLine());

                        switch (inventoryAction) {
                            case 1 -> playerInventory.displayInventory();
                            case 2 -> {
                                System.out.print("Masukkan item yang ingin ditambahkan: ");
                                String newItem = scanner.nextLine();
                                playerInventory.addItem(newItem);
                        }
                            case 3 -> {
                                System.out.print("Masukkan item yang ingin dihapus: ");
                                String itemToRemove = scanner.nextLine();
                                playerInventory.removeItem(itemToRemove);
                        }
                            case 4 -> {
                                playerInventory.clearInventory();
                                System.out.println("Inventaris telah dibersihkan.");
                        }
                            default -> System.out.println("Pilihan tidak valid.");
                        }
                        continue; 
                    }
                    case "5" -> {
                        System.out.println("5. Quit");
                        System.out.println(" ");                        
                        System.out.println("================= GAME SUDAH SELESAI DIMAINKAN !! =====================");
                        System.out.println("============ TERIMA KASIH SUDAH BERMAIN PERMAINAN INI !! ==============");
                       timer.cancel(); 
                       break OUTER;
                   }
                            default -> System.out.println("Perintah tidak valid. Ketik 'instruksi' untuk bantuan.");
                }
            }
        }
    }
    
public static final class Inventory {
    private List<String> items;

    public Inventory() {
        this.items = new ArrayList<>();
        addItem("Potion");
        addItem("Sword");
        addItem("Shield");
    }

    public void addItem(String item) {
        items.add(item);
    }

    public void removeItem(String item) {
        items.remove(item);
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

    public int getItemCount() {
        return items.size();
    }

    public String getItemAtIndex(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        } else {
            return null; 
        }
    }

    public void clearInventory() {
        items.clear();
    }

    public boolean containsItem(String item) {
        return items.contains(item);
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
        this.health = 100; 
    }

    public Player() {
        this.name = "Pemain Baru"; 
        this.health = 100; 
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}

interface GameEventListener {
    void onEnemyDefeated(String enemyName);
    void onGameFinish();
}

public static class Enemy {
    private final String name;
    private int health;
    private final GameEventListener eventListener; // Interface untuk menangani event

    public Enemy(String name, int health, GameEventListener listener) {
        this.name = name;
        this.health = health;
        this.eventListener = listener;
    }

        public String getName() {
            return name;
        }
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        if (health <= 0 && eventListener != null) {
            eventListener.onEnemyDefeated(this.name); // Memicu event jika musuh mati
        }
    }
}


    public static class BossEnemy extends Enemy {
        private final GameEventListener eventListener;

        public BossEnemy(String name, int health, GameEventListener eventListener) {
            super(name, health, eventListener);
            this.eventListener = eventListener;
        }

        @Override
        public void setHealth(int health) {
            if (health > 200) {
                System.out.println("Kesehatan Boss tidak bisa lebih dari 200");
            } else {
                super.setHealth(health);
            }

            if (health <= 0) {
                eventListener.onEnemyDefeated(getName());
            }
        }
    }
    
public static class Location {
    private final String description;

    public Location(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

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
