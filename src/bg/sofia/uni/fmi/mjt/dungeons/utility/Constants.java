package bg.sofia.uni.fmi.mjt.dungeons.utility;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final int ONE_HUNDRED = 100;
    public static final int NINETY = 90;
    public static final int EIGHTY = 80;
    public static final int SEVENTY = 70;
    public static final int SIXTY = 60;
    public static final int FIFTY = 50;
    public static final int FORTY = 40;
    public static final int THIRTY = 30;
    public static final int TWENTY = 20;
    public static final int FIFTEEN = 15;
    public static final int MAX_MINIONS_NUMBER = 10;
    public static final int MAX_TREASURES_NUMBER = 7;
    public static final int TEN = 10;
    public static final int FIFE = 5;
    public static final int FOUR = 4;
    public static final int THREE = 3;
    public static final int TWO = 2;
    public static final int ONE = 1;
    public static final int DEFENCE_MODIFIER = 5;
    public static final int ATTACK_MODIFIER = 5;

    public static final double UPGRADE_PERCENTAGE = 0.2;
    public static final double ZERO_POINT_TWO = 0.2;
    public static final double ZERO_POINT_ZEROFIVE = 0.05;
    public static final double ZERO_POINT_FIVE = 0.5;
    public static final double ZERO_POINT_THREE = 0.3;
    public static final double ZERO_POINT_ONE = 0.1;
    public static final double LEVEL_UP_MULTIPLIER = 0.2;

    public static final int ROWS = 7;
    public static final int COLUMNS = 12;
    public static final String JSON_CHARACTER_INFORMATION = "C:\\Users\\HP\\IdeaProjects\\Dungeons" +
            "\\src\\bg\\sofia\\uni\\fmi\\mjt\\dungeons\\utility\\CharacterInformation.json";
    public static final String JSON_MINION_INFORMATION = "C:\\Users\\HP\\IdeaProjects\\Dungeons" +
            "\\src\\bg\\sofia\\uni\\fmi\\mjt\\dungeons\\utility\\MinionsInformation.json";
    public static final String JSON_GAME_BOARD = "C:\\Users\\HP\\IdeaProjects\\Dungeons" +
            "\\src\\bg\\sofia\\uni\\fmi\\mjt\\dungeons\\utility\\GameBoard.json";

    public static final List<String> WEAPONS_NAMES = Arrays.asList(
            "Serpent Reaver", "Thunder Storm", "Leviathan Axe", "Blade of Olympus", "Excalibur",
            "Storm Breaker", "Celestial Edge", "Moon Sword", "Voidcleaver", "Narsil", "Sting",
            "Herugrim", "Morgul Blade", "Dropnir Spear", "Oathkeeper", "Rebelion", "Blackfyre"
    );

    public static final List<String> SPELL_NAMES = Arrays.asList(
            "Fireball", "Rasengan", "Chidori", "Necrotic Nightmare", "Eruption", "Super Nova",
            "Shadowclap", "Arcaneblaze", "Amaterasu", "Lunar Shadow", "Bleedwave", "Chaosflare",
            "Firerampage", "Celestial Storm", "Domain Expansion", "Lunar Frostbite", "Undyingrage"
    );
    public static final String EMPTY_STRING = "";
    public static final String SERVER_HOST = "localhost";
    public static final int SERVER_PORT = 7777;
}
