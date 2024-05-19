import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class BienImmobilier {
    private String type;
    private double taille;
    private double prixAchat;
    private double prixLocation;
    private String localisation;
    private String description;
    private AgentImmobilier agent;
    private boolean estLoue;

    public BienImmobilier(String type, double taille, double prixAchat, double prixLocation, String localisation,
            String description) {
        this.type = type;
        this.taille = taille;
        this.prixAchat = prixAchat;
        this.prixLocation = prixLocation;
        this.localisation = localisation;
        this.description = description;
        this.estLoue = false;
    }

    public String getDescription() {
        return description;
    }

    public boolean estLoue() {
        return estLoue;
    }

    public void louer() {
        estLoue = true;
    }

    public double getPrixAchat() {
        return prixAchat;
    }

    public double getPrixLocation() {
        return prixLocation;
    }

    public String getType() {
        return type;
    }

    public double getTaille() {
        return taille;
    }

    public String getLocalisation() {
        return localisation;
    }
}

class AgentImmobilier {
    private String nom;
    private List<BienImmobilier> biensGeres;

    public AgentImmobilier(String nom) {
        this.nom = nom;
        this.biensGeres = new ArrayList<>();
    }

    public void ajouterBien(BienImmobilier bien) {
        this.biensGeres.add(bien);
    }

    public List<BienImmobilier> getBiensGeres() {
        return biensGeres;
    }

    public String getNom() {
        return nom;
    }
}

class Client {
    private String nom;
    private Map<String, String> informations;
    private List<String> historiqueInteractions;

    public Client(String nom) {
        this.nom = nom;
        this.informations = new HashMap<>();
        this.historiqueInteractions = new ArrayList<>();
    }

    public void ajouterInformation(String cle, String valeur) {
        informations.put(cle, valeur);
    }

    public void ajouterInteraction(String interaction) {
        historiqueInteractions.add(interaction);
    }

    public String getNom() {
        return nom;
    }
}

class Transaction {
    private String type;
    private BienImmobilier bien;
    private Client client;
    private double montant;

    public Transaction(String type, BienImmobilier bien, Client client, double montant) {
        this.type = type;
        this.bien = bien;
        this.client = client;
        this.montant = montant;
    }

    public String getType() {
        return type;
    }

    public BienImmobilier getBien() {
        return bien;
    }

    public Client getClient() {
        return client;
    }

    public double getMontant() {
        return montant;
    }
}

public class Main {
    private static final double CONVERSION_RATE = 3.3; // 1 euro = 3.3 dinars
    private static Scanner scanner = new Scanner(System.in);
    private static AgentImmobilier agent1 = new AgentImmobilier("Jean");
    private static List<Transaction> transactions = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Bienvenue dans notre agence immobilière !");
        String localisation = demanderLocalisation();

        BienImmobilier bien1 = new BienImmobilier("Appartement", 80.0, 100000.0, 800.0, localisation,
                "Bel appartement près du centre-ville");
        BienImmobilier bien2 = new BienImmobilier("Maison", 120.0, 200000.0, 1200.0, localisation,
                "Grande maison avec jardin");
        agent1.ajouterBien(bien1);
        agent1.ajouterBien(bien2);

        Client client1 = creerClient();

        listerBiensDisponibles();

        gererTransaction(client1);

        scanner.close();
    }

    private static String demanderLocalisation() {
        System.out.println("Où souhaitez-vous acheter ou louer une maison ?(hydra,kouba,cheraga)");
        return scanner.nextLine();
    }

    private static Client creerClient() {
        System.out.println("Entrez le nom du client :");
        String nomClient = scanner.nextLine();
        Client client = new Client(nomClient);

        System.out.println("Entrez l'email du client :");
        String email = scanner.nextLine();
        client.ajouterInformation("Email", email);

        System.out.println("Entrez le téléphone du client :");
        String telephone = scanner.nextLine();
        client.ajouterInformation("Téléphone", telephone);

        return client;
    }

    private static void listerBiensDisponibles() {
        System.out.println("Voici les biens disponibles pour l'achat et la location :");
        for (BienImmobilier bien : agent1.getBiensGeres()) {
            String status = bien.estLoue() ? "Loué" : "Disponible";
            System.out.println("Type: " + bien.getType() + ", Taille: " + bien.getTaille() + " m², Localisation: "
                    + bien.getLocalisation() +
                    ", Prix d'achat: " + bien.getPrixAchat() + " euros, Prix de location: " + bien.getPrixLocation()
                    + " euros par mois, Status: " + status);
        }
    }

    private static void gererTransaction(Client client) {
        System.out.println("Souhaitez-vous acheter ou louer un bien ? (achat/louer)");
        String choix = scanner.nextLine();
        System.out.println("Veuillez sélectionner le type de bien : ");
        String typeBien = scanner.nextLine();

        BienImmobilier bienSelectionne = null;
        for (BienImmobilier bien : agent1.getBiensGeres()) {
            if (bien.getType().equalsIgnoreCase(typeBien)) {
                bienSelectionne = bien;
                break;
            }
        }

        if (bienSelectionne == null) {
            System.out.println("Aucun bien de ce type n'est disponible.");
            return;
        }

        switch (choix.toLowerCase()) {
            case "achat":
                double prixAchatDinars = bienSelectionne.getPrixAchat() * CONVERSION_RATE;
                System.out.println("Le prix d'achat du " + bienSelectionne.getType() + " est : "
                        + bienSelectionne.getPrixAchat() + " euros (" + prixAchatDinars + " dinars).");
                System.out.println("À quelle échéance souhaitez-vous effectuer le paiement ? (ex: 30 jours)");
                String paiementTime = scanner.nextLine();
                transactions.add(new Transaction("achat", bienSelectionne, client, bienSelectionne.getPrixAchat()));
                System.out.println("Transaction d'achat effectuée.");
                System.out.println("Détails du bien acheté :");
                System.out.println("Type: " + bienSelectionne.getType() + ", Taille: " + bienSelectionne.getTaille()
                        + " m², Localisation: " + bienSelectionne.getLocalisation() +
                        ", Prix d'achat: " + bienSelectionne.getPrixAchat() + " euros, Description: "
                        + bienSelectionne.getDescription());
                System.out.println("Paiement prévu dans " + paiementTime);
                break;
            case "louer":
                if (!bienSelectionne.estLoue()) {
                    bienSelectionne.louer();
                    double prixLocationDinars = bienSelectionne.getPrixLocation() * CONVERSION_RATE;
                    System.out.println("Le prix de location du " + bienSelectionne.getType() + " est : "
                            + bienSelectionne.getPrixLocation() + " euros par mois (" + prixLocationDinars
                            + " dinars par mois).");
                    transactions
                            .add(new Transaction("louer", bienSelectionne, client, bienSelectionne.getPrixLocation()));
                    System.out.println("Félicitations ! Vous avez loué le " + bienSelectionne.getType() + " situé à "
                            + bienSelectionne.getDescription() + ".");
                } else {
                    System.out.println("Désolé, le " + bienSelectionne.getType() + " est déjà loué.");
                }
                break;
            default:
                System.out.println("Choix invalide.");
                break;
        }
    }
}