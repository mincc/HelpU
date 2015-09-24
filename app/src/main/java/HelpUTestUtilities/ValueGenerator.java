package HelpUTestUtilities;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.UUID;


/**
 * Created by Chung Min on 8/23/2015.
 */
public class ValueGenerator {

    private final static String randomStringChars = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ0123456789!";

    private final static String randomStringOnlyLowercaseChars = "abcdefghijkmnopqrstuvwxyz";

    private static String[] userNameStarts = {
            "fred", "spanky", "ralph", "kazza", "frank", "michelle", "suz", "bat", "dingo",
            "charlie", "lamp", "dog", "plastic", "garlic", "sky", "train", "big", "the"};

    private static String[] userNameInbetweens = {
            ".", "_", "", "", "", "", ""};

    private static String[] userNameEnds = {
            "23", "79", "1985", "995", "105", "lover", "tastesbad", "eater", "touch", "sale",
            "lovesit", "touched", "angel", "hair", "eyes", "ears", "tippy", "atro",
            "attacked", "avaricia", "autistk", "augiedog", "ayandys", "andatron", "aeration", "ardwynna",
            "albuy", "archie", "ankitty", "aesli", "ailok", "annikki", "aossesc", "awzshep",
            "atryland", "angelion", "angelmlf", "aratron", "ambera", "aristol", "azdragon", "amethomp",
            "annstein", "annissa", "amyyuy", "adorai", "aeontrin", "avbud", "alixx", "aldaira",
            "aspitin", "apham", "abadabba", "amps", "almagro", "anniejo", "alvarra", "amnabors",
            "acunjic", "kipu", "kaymcee", "kevcops", "karad", "karaella", "kikikaka", "karasue",
            "knchan", "kcjo", "kinaetic", "kaybie", "kalmire", "kuishin", "kmills", "katechi",
            "kasa", "kheldrae", "kiichan", "korvinus", "kcukoo", "koreehz", "kidcfc", "kcrapton",
            "kgbrider", "ex_karst576", "keishy", "kudichan", "kaboink", "kccttzj", "kaodrama", "khio",
            "kellzie", "kvl", "kiwitan", "kawazoe", "kariu", "krokas", "koek", "katiez", "kristioh", "klavdia", "krozteca"};

    private static String[] firstNames = {
            "William John", "David", "Thomas", "Jack", "John", "Peter", "Paul", "James", "Thomas",
            "George", "William", "Andrew", "Jack", "James", "Thomas", "Brian", "Mark", "Daniel", "Daniel",
            "Arthur", "David", "John", "Matthew", "Joshua", "Mary", "Margaret", "Susan", "Rebecca", "Chloe",
            "Florence", "Jean", "Julie", "Lauren", "Emily", "Doris", "Mary", "Karen", "Jessica", "Megan",
            "Edith", "Joan", "Jacqueline", "Charlotte", "Jessica", "Dorothy", "Patricia", "Deborah", "Hannah", "Sophie",
            "Daina", "Creola", "Carli", "Camie", "Bunny", "Brittny", "Ashanti", "Anisha", "Aleen", "Adah", "Yasuko", "Winter", "Viki", "Valrie",
            "Tona", "Tinisha", "Thi", "Terisa", "Tatum", "Taneka", "Simonne", "Shalanda", "Serita", "Ressie", "Refugia",
            "Paz", "Olene", "Na", "Merrill", "Margherita", "Mandie", "Man", "Maire", "Lyndia", "Luci", "Lorriane",
            "Loreta", "Leonia", "Lavona", "Lashawnda", "Lakia", "Kyoko", "Krystina",
            "Krysten", "Kenia", "Kelsi", "Jude", "Jeanice", "Isobel", "Georgiann",
            "Genny", "Felicidad", "Eilene", "Deon", "Deloise", "Deedee", "Dannie",
            "Conception", "Clora", "Cherilyn", "Chang", "Calandra", "Berry", "Armandina",
            "Anisa", "Ula", "Timothy", "Tiera", "Theressa", "Stephania", "Sima", "Shyla",
            "Shonta", "Shera", "Shaquita", "Shala", "Sammy", "Rossana", "Nohemi", "Nery",
            "Moriah", "Melita", "Melida", "Melani", "Marylynn", "Marisha", "Mariette",
            "Malorie", "Madelene", "Ludivina", "Loria", "Lorette", "Loralee", "Lianne",
            "Leon", "Lavenia", "Laurinda", "Lashon", "Kit", "Kimi", "Keila", "Katelynn",
            "Kai", "Jone", "Joane", "Ji", "Jayna", "Janella", "Ja", "Hue", "Hertha", "Francene",
            "Elinore", "Despina", "Delsie", "Deedra", "Clemencia", "Carry", "Carolin", "Carlos",
            "Bulah", "Brittanie", "Bok", "Blondell", "Bibi", "Beaulah", "Beata", "Annita", "Agripina",
            "Virgen", "Valene", "Un", "Twanda", "Tommye", "Toi", "Tarra", "Tari", "Tammera", "Shakia",
            "Sadye", "Ruthanne", "Rochel", "Rivka", "Pura", "Nenita", "Natisha", "Ming", "Merrilee",
            "Melodee", "Marvis", "Lucilla", "Leena", "Laveta", "Larita", "Lanie", "Keren", "Ileen",
            "Georgeann", "Genna", "Genesis", "Frida", "Ewa", "Eufemia", "Emely", "Ela", "Edyth",
            "Deonna", "Deadra", "Darlena", "Chanell", "Chan", "Cathern", "Cassondra", "Cassaundra",
            "Bernarda", "Berna", "Arlinda", "Anamaria", "Albert", "Wesley", "Vertie", "Valeri", "Torri",
            "Tatyana", "Stasia", "Sherise", "Sherill", "Jefferey", "Ahmed", "Willy", "Stanford", "Oren",
            "Noble", "Moshe", "Mikel", "Enoch", "Brendon", "Quintin", "Jamison", "Florencio", "Darrick",
            "Tobias", "Minh", "Hassan", "Giuseppe", "Demarcus", "Cletus", "Tyrell", "Lyndon", "Keenan",
            "Werner", "Theo", "Geraldo", "Lou", "Columbus", "Chet", "Bertram", "Markus", "Huey", "Hilton",
            "Dwain", "Donte", "Tyron", "Omer", "Isaias", "Hipolito", "Fermin", "Chung", "Adalberto",
            "Valentine", "Jamey", "Bo", "Barrett", "Whitney", "Teodoro", "Mckinley", "Maximo", "Garfield",
            "Sol", "Raleigh", "Lawerence", "Abram", "Rashad", "King", "Emmitt", "Daron", "Chong", "Samual",
            "Paris", "Otha", "Miquel", "Lacy", "Eusebio", "Dong", "Domenic", "Darron", "Buster", "Antonia",
            "Wilber", "Renato", "Jc", "Hoyt", "Haywood", "Ezekiel", "Chas", "Florentino", "Elroy", "Clemente",
            "Arden", "Neville", "Kelley", "Edison", "Deshawn", "Carrol", "Shayne", "Nathanial", "Jordon",
            "Danilo", "Claud", "Val", "Sherwood", "Raymon", "Rayford", "Cristobal", "Ambrose", "Titus",
            "Hyman", "Felton", "Ezequiel", "Erasmo", "Stanton", "Lonny", "Len", "Ike", "Milan", "Lino", "Jarod",
            "Herb", "Andreas", "Walton", "Rhett", "Palmer", "Jude", "Douglass", "Cordell", "Oswaldo", "Ellsworth",
            "Virgilio", "Toney", "Nathanael", "Del", "Britt", "Benedict", "Mose", "Hong", "Leigh", "Johnson", "Isreal",
            "Gayle", "Garret", "Fausto", "Asa", "Arlen", "Zack", "Warner", "Modesto", "Francesco", "Manual", "Jae", "Gaylord",
            "Gaston", "Filiberto", "Deangelo", "Michale", "Granville", "Wes", "Malik", "Zackary", "Tuan", "Nicky",
            "Eldridge", "Cristopher", "Cortez", "Antione", "Malcom", "Long", "Korey", "Jospeh", "Colton", "Waylon",
            "Von", "Hosea", "Shad", "Santo", "Rudolf", "Rolf", "Rey", "Renaldo", "Marcellus", "Lucius", "Lesley",
            "Kristofer", "Boyce", "Benton", "Man", "Kasey", "Jewell", "Hayden", "Harland", "Arnoldo", "Rueben",
            "Leandro", "Kraig", "Jerrell", "Jeromy", "Hobert", "Cedrick", "Arlie", "Winford", "Wally",
            "Patricia", "Luigi", "Keneth", "Jacinto", "Graig", "Franklyn", "Edmundo", "Sid", "Porter", "Leif",
            "Lauren", "Jeramy", "Elisha", "Buck"
    };

    private static String[] lastNames = {
            "Smith", "Matthews", "Brumby", "Lunson", "Sharman", "Graham", "Best",
            "Kerslake", "Vagg", "Cox", "Brown", "Sinclair", "Jones", "Dobson",
            "Koning", "Lamprey", "Williams", "Walters", "Aylett", "Bessant",
            "Wilson", "Meddings", "Mitchell", "Cooper", "Edwards", "Bonney",
            "Johnson", "Wise", "Murfet", "Robinson", "Daniells", "Maloney",
            "Martin", "Hodgetts", "Russell", "Padman", "Butler", "Holmes",
            "Richards", "Barker", "Hall", "Stokes", "Walker", "Anderson",
            "Howard", "Saltmarsh", "Harris", "Hingston", "Brooks", "Burgess",
            "Hamby", "Boyles", "Boles", "Regan", "Faust", "Crook", "Beam", "Barger",
            "Hinds", "Gallardo", "Elias", "Willoughby", "Willingham", "Wilburn", "Eckert",
            "Busch", "Zepeda", "Worthington", "Tinsley", "Russ", "Li", "Hoff", "Hawley", "Carmona",
            "Varela", "Rector", "Newcomb", "Mallory", "Kinsey", "Dube", "Whatley", "Strange",
            "Ragsdale", "Ivy", "Bernstein", "Becerra", "Yost", "Mattson", "Ly", "Felder", "Cheek",
            "Luke", "Handy", "Grossman", "Gauthier", "Escobedo", "Braden", "Beckman", "Mott",
            "Hillman", "Gil", "Flaherty", "Dykes", "Doe", "Stockton", "Stearns", "Lofton",
            "Kitchen", "Coats", "Cavazos", "Beavers", "Barrios", "Tang", "Parish", "Mosher",
            "Lincoln", "Cardwell", "Coles", "Burnham", "Weller", "Lemons", "Beebe", "Aguilera",
            "Ring", "Parnell", "Harman", "Couture", "Alley", "Schumacher", "Redd", "Dobbs",
            "Blum", "Blalock", "Merchant", "Ennis", "Denson", "Cottrell", "Chester", "Brannon",
            "Bagley", "Aviles", "Watt", "Sousa", "Rosenthal", "Rooney", "Dietz", "Blank", "Paquette",
            "Mcclelland", "Duff", "Velasco", "Lentz", "Grubb", "Burrows", "Barbour", "Ulrich",
            "Shockley", "Rader", "German", "Beyer", "Mixon", "Layton", "Altman", "Alonzo", "Weathers",
            "Titus", "Stoner", "Squires", "Shipp", "Priest", "Lipscomb", "Cutler", "Caballero",
            "Zimmer", "Willett", "Thurston", "Storey", "Medley", "Lyle", "Epperson", "Shah",
            "Mcmillian", "Baggett", "Torrez", "Laws", "Hirsch", "Dent", "Corey", "Poirier",
            "Peachey", "Jacques", "Farrar", "Creech", "Barth", "Trimble", "France", "Dupre", "Albrecht",
            "Sample", "Lawler", "Crisp", "Conroy", "Chadwick", "Wetzel"
    };

    private static String[] emailHosts = {
            "gmail.com", "hotmail.com", "hotmale.com", "yahoo.com",
            "westnet.com.au", "bigpond.com", "loopwireless.com",
            "abc.net.au", "abc.com", "bbc.co.uk", "cnbc.com", "intel.com",
            "three.co.uk", "three.com.au", "google.com", "google.com.au",
            "yahoo.co.uk", "yahoo.com.au", "yahoo.com.sg", "amazon.com",
            "amazon.co.uk", "paypal.com", "paypal.com.au", "ebay.com"
    };

    private static String[] verbs = {
            "catching", "riding", "flapping", "eating", "walking", "singing", "sitting",
            "listening", "relaxing", "laying", "kissing", "jumping", "sniffing"
            , "chilling", "wondering", "calling", "talking", "yapping", "fishing", "laughing"
    };

    private static String[] adjectives = {
            "his", "her", "the", "my", "our", "a", "nice", "hairy", "smelly", "lovely"
    };

    private static String[] nouns = {
            "nose", "dog", "cat", "elbow", "chair", "road", "mouse", "fence", "pill", "v8", "turbo", "belly",
            "woman", "man", "door", "chilli", "kangaroo", "koala", "echidna", "wombat", "potaroo"
            , "girl", "guy", "swan", "eagle", "bird", "pig", "cheetah", "tiger", "lion", "bell", "cake", "frog"
    };

    private static String[] tags = {
            "activism", "adventure", "animals", "appearance", "art", "beach", "beauty", "bike", "blog", "body", "book",
            "books", "business", "buy", "camera", "car", "career", "challenge", "change", "charity", "cheers", "children", "clean", "cleaning",
            "clothes", "college", "communication", "community", "computer", "computers", "concert", "confidence", "cooking", "craft", "crafts",
            "create", "creative", "creativity", "culture", "daily", "dance", "design", "development", "diet", "discipline", "dream", "dreams",
            "education", "energy", "entertainment", "environment", "europe", "exercise", "experience", "expression", "faith", "family", "fashion",
            "fear", "fiction", "film", "finance", "finances", "financial", "fitness", "food", "free", "freedom", "friend", "friends", "friendship", "fun",
            "future", "games", "garden", "goal", "goals", "god", "growth", "guitar", "hair", "happiness", "happy", "health", "healthy", "help", "hiking",
            "history", "hobbies", "hobby", "home", "house", "improvement", "independence", "inspiration", "internet", "japan", "job", "joy", "kids",
            "knitting", "knowledge", "language", "languages", "learn", "learning", "life", "lifestyle", "literature", "live", "living", "lose", "weight",
            "love", "marriage", "meals", "meditation", "mind", "money", "move", "movie", "movies", "music", "nature", "novel", "organization", "organize",
            "outdoors", "paint", "painting", "party", "passion", "peace", "people", "personal", "personal-development", "personal-growth", "pets",
            "philosophy", "photography", "photos", "pictures", "play", "poetry", "politics", "productivity", "programming", "read", "reading", "recreation",
            "relationship", "relationships", "relax", "relaxation", "religion", "romance", "run", "running", "school", "self", "self-improvement",
            "self-improvement", "sewing", "sex", "sexy", "shopping", "skill", "skills", "sleep", "social", "software", "spirit", "spiritual",
            "spirituality", "sport", "sports", "strength", "stress", "study", "success", "technology", "time", "travel",
            "television", "university", "vacation", "volunteer", "wacky", "water", "web", "website", "weight", "weight", "loss", "work", "world",
            "write", "writing", "yoga"};

    private static String[] videoFilenames = {
            "a.3gp", "b.3gp", "c.3gp", "d.3gp", "e.3gp", "f.3gp", "g.3gp", "h.3gp"};

    private static String[] imageFilenames = {
            "1.jpg", "2.jpg", "3.jpg", "4.jpg", "5.jpg", "6.jpg", "7.jpg", "8.jpg"};

    private static String[] audioFilenames = {
            "1.mp3", "2.mp3", "3.mp3", "4.wma", "5.mp3", "6.ogg", "7.mp3", "8.mp3"};

    private static String[] randomMessageStrings = {
            "Lorem ipsum dolor sit amet,", "consectetuer adipiscing elit.",
            "Nullam semper dui non tortor.", "Proin sed ipsum vitae augue aliquet sollicitudin.",
            "Ut ornare,", "dolor at varius viverra,", "magna nisl mollis erat,",
            "vel felis mauris et lorem.", "Morbi nisl pede,",
            "porta id,", "egestas sit amet,", "faucibus at,",
            "nulla.", "Vestibulum nisl.", "Duis pellentesque auctor metus.",
            "Fusce quis libero in tellus pellentesque tristique.",
            "Suspendisse non magna.", "Mauris porttitor sem quis justo.",
            "In bibendum sapien.", "Morbi risus tellus,", "porttitor eu,",
            "commodo id,", "iaculis vel,", "lectus.", "Aenean gravida adipiscing dui.",
            "Aenean commodo nunc id nunc commodo aliquet.", "Cras dui metus,",
            "aliquam a,", "luctus bibendum,", "malesuada vitae,", "orci.",
            "Aliquam suscipit felis id dui.", "Sed felis sem,", "vehicula eu,",
            "tristique non,", "tristique ac,", "metus.", "Ut ut orci ut diam scelerisque semper.",
            "Morbi laoreet tellus ut mi.", "Nam justo.", "Fusce ultricies."
    };

    private static String[] randomPhrases = {
            "The parking lot",
            "A chain saw",
            "Another inferiority complex",
            "The paycheck is snooty.",
            "A burly lover",
            "The eggplant near the carpet tack",
            "A line dancer near a pine cone",
            "The worldly warranty",
            "A chestnut",
            "A blithe spirit beyond a satellite",
            "A cloth loses the go brass under the wind.",
            "The notable camp fiddles.",
            "A plotter gasps beside our assistant!",
            "Within the physical recorder exists the swamped torture.",
            "Whatever parrot coughs across the pupil",
            "The resolve attends above every immune concentrate.",
            "Underneath the comic foams whatever closure.",
            "Its civil hobby towers.",
            "Why won't the transported entrance reconcile the breach?",
            "When can a staggering species school a sailing thoroughfare?",
            "Any pursued melody chooses.",
            "An overpriced basket tiles the imposed process.",
            "Her stereo shelves his chief against the unknown beginner.",
            "The stagger grasses the hardware within the intent studio.",
            "The mud cooperates inside the teenager.",
            "With the spiral reasons a patent typewriter.",
            "An informative gnome treks over the combined elephant.",
            "The nameless alien breezes.",
            "The headline laughs opposite a closure!",
            "The maker shelves an infrequent annoyance after the milk.",
            "An adjusted drum experiments.",
            "Beside the expiring carbon glows the horrific gentleman.",
    };

    private static String[] randomReportPrefixStrings = {
            "daily", "monthly", "quaterly", "weekly", "yearly"
    };

    private static String[] randomArticleParagraphs = {
            "Angelina Jolie has allegedly threatened to dump lover Brad Pitt after discovering he'd taken their baby daughter, Shiloh, to meet his ex-wife Jennifer Aniston.",
            "And Sting and his wife Trudie Styler have been ordered to pay their former chef compensation. She alleges she was sacked after she fell pregnant. The star couple are said to be appealing.",
            "Oscar-winnng actor Russell Crowe and wife Danielle Spencer graced the red carpet at a do for the Australian Children's Music Foundation.",
            "Police are investigating whether celebrity heiress Paris Hilton got special treatment during her 23 days jail term. But LA County Sheriff Lee Baca says the probe is wasting taxpayers' dollars on something really trivial.",
            "Former US teen queen Lindsay Lohan has finished her 45-day rehab program.The star's rep says Lindsay's now in an intensive outpatient program and has agreed to wear an alcohol monitoring bracelet on her ankle.She's doing great. Lindsay is working hard on her sobriety and we are all supporting her.",
            "And Victoria Beckham hasn't got much love for US comedian Eddie Murphy after he dumped Spice Girls bandmate Melanie 'Scary Spice' Brown while she was pregnant with his child.",
            "Actress Evan Rachel Wood says she's annoyed about reports she's the reason Marilyn Manson split from wife Dita Von Teese. She say she's not a homewrecker.",
            "Hollywood actor Charlie Sheen's engaged to his girlfriend, real estate investor Brooke Mueller. His ex-wife Denise Richards reportedly sent flowers to congratulate the couple.",
            "Some exciting news from Down Under - Toni Collette has revealed she is with child. It's hers and hubby Dave Galafassi first.",
            "Top Gear presenter Jeremy Clarkson isn't winning over any environmentally conscious fans after the team's reported antics in Botswana.",
            "When an ocean living with the submarine laughs out loud, the unstable recliner hibernates.",
            "Sometimes the ocean behind the ocean goes to sleep, but a traffic light behind a blithe spirit always sanitizes a corporation of a squid!",
            "When you see a briar patch toward the chain saw, it means that a photon of the scythe goes to sleep.",
            "An umbrella slyly reaches an understanding with a rattlesnake living with the steam engine, but a cab driver hesitantly is a big fan of an insurance agent.",
            "Sometimes the girl scout related to a squid returns home, but a movie theater always completely bestows great honor upon a crank case!",
            "Some cowboy ridiculously sells the fashionable sheriff to a crispy short order cook.",
            "When the light bulb rejoices, the vacuum cleaner starts reminiscing about lost glory.",
            "A hockey player related to a grain of sand laughs and drinks all night with a tomato toward a hockey player, because a dirt-encrusted freight train is a big fan of the diskette for the fundraiser. Any ball bearing can wisely operate a small fruit stand with another highly paid salad dressing, but it takes a real hydrogen atom to dance with a jersey cow.",
            "For example, the customer about another tabloid indicates that the squid near a pit viper borrows money from a tomato related to a recliner.",
            "Another support group, an overripe mortician, and a ski lodge are what made America great!",
            "A light bulb inside the chess board buries the psychotic oil filter.",
            "When the loyal blood clot rejoices, a blood clot around the customer prays.",
            "A vacuum cleaner panics, because some insurance agent inside an anomaly graduates from a college-educated fighter pilot.",
            "When a crispy spider is single-handledly crispy, some alleged turkey finds lice on the prime minister behind a tomato.",
            "Furthermore, an accidentally raspy cyprus mulch flies into a rage, and a surly tornado usually goes deep sea fishing with a cargo bay defined by the diskette.",
            "For example, a submarine inside a paper napkin indicates that a turn signal reaches an understanding with a cocker spaniel behind a short order cook.",
            "Another cloud formation for a hockey player earns frequent flier miles, but the slyly snooty mortician avoids contact with a federal tomato.",
            "Now and then, the hydrogen atom requires assistance from a wisely moronic judge.",
            "Sometimes a jersey cow leaves, but a mean-spirited salad dressing always learns a hard lesson from a girl scout!",
            "Furthermore, some pathetic burglar goes to sleep, and the wisely greasy defendant makes love to an eagerly moronic hockey player.",
            "A fighter pilot around a tripod eats a blood clot beyond some recliner.",
            "Any ball bearing can often find subtle faults with a scythe, but it takes a real garbage can to negotiate a prenuptial agreement with another globule."
    };

    private static String[] randomArticlePadding = {
            "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Donec lacus magna, ultrices eget, lacinia at, congue vitae, sapien. Sed at lacus. Mauris commodo feugiat tellus. Sed dui. Integer et mauris. Nunc at lectus eu nibh convallis euismod. Aenean gravida lobortis lectus. Maecenas pede purus, aliquam sit amet, tempus sollicitudin, ultrices suscipit, lectus. Integer mi est, adipiscing nec, feugiat sit amet, nonummy id, ipsum. Nam justo. Nam vel nunc. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Praesent sit amet turpis. Sed porttitor, augue quis vehicula convallis, pede nunc dictum risus, ut egestas leo pede et felis. Donec dapibus orci vel elit. Integer tristique viverra velit. Duis tempus, sem eget dictum convallis, odio velit posuere justo, ac suscipit ipsum nisi a tortor. In hac habitasse platea dictumst.",
            "Aliquam convallis nisl a est. Morbi sit amet mauris a sapien dapibus tincidunt. Fusce ut diam volutpat erat accumsan consectetuer. Nullam magna risus, vehicula at, ornare sit amet, sollicitudin vel, nisl. Mauris orci nibh, volutpat a, vulputate id, pellentesque eget, magna. Fusce ante lorem, adipiscing iaculis, molestie nec, feugiat ut, ipsum. Aenean viverra euismod libero. Sed aliquet posuere lacus. Donec vestibulum erat sit amet ligula. Nullam ac est. Suspendisse potenti. Integer odio velit, faucibus eget, vestibulum eget, elementum id, ante. Quisque euismod.",
            "Nulla porta sem tincidunt urna. Nunc enim justo, dignissim et, elementum eget, feugiat quis, velit. Cras tortor. Quisque nisi mi, porta varius, blandit ut, sodales eget, nibh. Vivamus nisl. Pellentesque fringilla justo at massa. Pellentesque sed dolor sed justo gravida ornare. Phasellus vitae sapien. Aliquam mattis facilisis ligula. Proin bibendum mauris ut risus. Sed suscipit sapien eu libero. Vivamus tristique turpis vel justo. Donec quis tellus quis massa sodales commodo.",
            "Phasellus consequat, eros pellentesque pretium vulputate, augue tortor elementum nunc, nec aliquet erat massa at erat. Phasellus sodales, turpis sed dictum eleifend, nibh orci vehicula enim, et rutrum velit sapien semper diam. Nunc volutpat bibendum felis. Quisque augue. Donec vestibulum porta lorem. Cras sed ipsum. Mauris sit amet urna vel dolor tristique feugiat. Donec sagittis eros sed quam. Nunc lacinia placerat diam. Proin ornare massa et elit. Curabitur tincidunt accumsan massa. Nullam sit amet lorem sed erat euismod tempor. Pellentesque ut sem. Etiam pulvinar nulla in nisi. Fusce aliquam nisl gravida tellus. Quisque lectus nunc, posuere vel, hendrerit ac, nonummy ut, ipsum.",
            "Integer volutpat egestas velit. Mauris facilisis dui id nibh. In eget nunc. Morbi faucibus, elit et condimentum consectetuer, dolor felis ultrices nibh, vitae porttitor nunc diam eget arcu. Praesent fermentum mauris. Mauris purus dolor, auctor ac, venenatis non, tempor in, dui. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Mauris euismod elit ac turpis. Duis tempus. Proin a est. Nam vestibulum, tortor ut suscipit interdum, magna mi hendrerit elit, sit amet faucibus diam dolor a diam. Nulla neque libero, rutrum vitae, vulputate sollicitudin, hendrerit in, orci. Aenean mollis sem a metus. Mauris lobortis. Duis tincidunt turpis sed erat. Nulla iaculis sapien sit amet justo. Integer adipiscing ipsum sed sapien. Vivamus eleifend, justo in convallis mattis, enim nisi eleifend risus, et pellentesque enim orci at ligula. Nulla metus lectus, feugiat vitae, vehicula gravida, tincidunt eu, libero."
    };

    private static String[] randomShopAddress = {
            "4th Floor, Millennium Office Block, 160, Jalan Bukit Bintang, Kuala Lumpur",
            "No. 25, Jalan Persiaran Taiping, Taiping",
            "Lot F121,Jalan Besar Bee Farm,Tringkap, Cameron Highland",
            "Lot 9120 & 9121, Jalan Tepi Sungai, Hutan Melintang",
            "Lot No:LG070, Lower Ground Floor, Mid Valley City, Lingkran Syed Putra",
            "20 & 22 Jalan Istimewa 4, Taman Perindustrian Cemerlang, Ulu Tiram",
            "No. 53, SS2/64, Petaling Jaya",
            "LG-94, Holiday Plaza, Jalan Dato Sulaiman, Century Garden",
            "Lot No:210a, Lower Ground Floor, Promenade 1 Utama Shoppng Centre, 1 Dataran Bandar Utama, Bandar Utama, Petaling Jaya",
            "170-B1-09 , Gurney Plaza, Gurney Drive",
            "Lot No:G45 Metro Prima Shopping Centre No.1 Jalan Metro Prima, Kepong",
            "306 Jalan Burma, Pulau Tikus",
            "35 Jalan Meldrum",
            "26, Jalan Perindustrail PBP3, Taman Perindustrian Pusat Bandar Puchong",
            "32 Jalan Tiara 4, Bandar Baru Klang, Klang",
            "6, Jalan 4/118C, Taman Desa Tun Razak, Cheras",
            "8-20-1, Jalan 15/155B, Aked Esplanad Bukit Jalil, Kuala Lumpur",
            "34 (35A), Tingkat Bawah, Jalan Dato Yusof Shahbudin 28, Taman Sentosa, Klang",
            "22, Jalan Dua, Taman Sri Berjuntai, Batang Berjuntai",
            "12, Jalan Muda 24, Off Jalan Meru, Klang",
            "No: 16, 17, Jalan Ipoh, Batu18, Rawang",
            "Suite 991, MBE Taipan 51G, Jalan USJ 10/1, Subang Jaya",
            "Lot 37 & 38, Masjid Tanah Industrial Estate, Masjid Tanah",
            "26 SS2/61, Petaling Jaya",
            "No. 29A, Jalan 2C, Kampung Baru Subang, Batu 3, Shah Alam",
            "30-34 Jalan Industri USJ1/13, Taman Perindustrian USJ 1, Subang Jaya",
            "81-02, Jalan Rosmerah 2/16, Taman Johor Jaya, Johor Bahru",
            "16, Jalan Industri Mas 12, Taman Puchong Mas, Puchong",
            "No. 19, Jalan TPP 1/1A, Taman Industri Puchong, Batu 12, Jalan Puchong, Puchong",
            "No. 307-C, Jalan Pasir Bedamar, Teluk Intan ",
            "12, Jalan Segamput Lentang",
            "Lot 945, Kampung Bohor Rendam, Mukim Bohor, Langkawi",
            "No. 19, Jalan Gangsa SD5/3F, Sri Damansara, Kuala Lumpur, Malaysia.",
            "Lot 6, Persiaran Perusahaan, Seksyen 23, Kawasan Perusahaan Shah Alam, Shah Alam",
            "31, Jalan 25/32, Seksyen 25, Shah Alam",
            "21G, Jalan Pandan Indah 1/23B, Pandan Indah",
            "Lot 6, Persiaran Perusahaan, Seksyen 23, Kawasan Perusahaan Shah Alam, Shah Alam",
            "6, 8, 10 & 14, Lintang Sungai Keramat 6A, Taman Klang Utama, Klang",
            "Kampung Kebakat, Wakaf Bharu,",
            "No. 2047, Jalan Padang, Jinjang Utara",
            "B-3-19, Pusat Perdagangan Pelangi, PJU 6, Persiaran Surian, Petaling Jaya",
            "13, Sec 19/1, PJ",
            "No.27, Jalan Dedap 6, Taman Johor Jaya, Johor Bahru",
            "29-33, Jalan Nagasari 18, Taman Segamat Baru, Segamat",
            "5, Jalan Lama, Muar",
            "84, Jalan Kapar 27/89, Megah Industrial Park, Seksyen 27, Shah Alam",
            "No. 30, Jalan Kebun Nenas, 1C/KS 7, Bandar Putera, Klang",
            "21-7, Jalan SP 2/1, Taman Serdang Perdana, Seksyen 2, Seri Kembangan",
            "21-0-20, Jalan 15/2A, Taman Wilayah, Batu Caves"
    };

    private static String[] randomTopLevelDomains = {
            "aero", "asia", "biz", "cat", "com", "coop", "edu", "gov", "info", "int", "jobs", "mil",
            "mobi", "museum", "name", "net", "org", "pro", "tel", "travel", "ad", "ae", "af", "ag", "ai",
            "al", "am", "ao", "aq", "ar", "as", "at", "au", "aw", "ax", "az", "ba", "bb", "bd", "be",
            "bf", "bg", "bh", "bi", "bj", "bl", "bm", "bn", "bo", "bq", "br", "bs", "bt", "bv", "bw", "by", "bz"
    };

    private static String[] randomTimeFoodDelivery = {
            "08.00am - 10.00pm", "09.00am - 10.00pm", "10.00am - 10.00pm",
            "02.00pm - 05.00pm", "12.00pm - 01.00pm", "05.00pm - 12.00am"
    };

    /// <summary>
    /// Gets a random String of a random length.
    /// </summary>
    /// <param name="minLength"></param>
    /// <param name="maxLength"></param>
    /// <returns></returns>
    public static String getRandomString(short minLength, short maxLength) {
        return getRandomString((short) getRandomInt(minLength, maxLength));
    }

    /// <summary>
    /// Gets a random String using a specific set of characters.
    /// </summary>
    /// <param name="length"></param>
    /// <param name="allowedChars"></param>
    /// <returns></returns>
    public static String getRandomString(short length, String allowedChars) {
        String randomString = "";

        for (int i = 0; i <= length - 1; i++) {
            randomString += allowedChars.charAt(getRandomInt(0, allowedChars.length() - 1));
        }

        return randomString;
    }

    /// <summary>
    /// Gets a random String.
    /// </summary>
    /// <param name="length"></param>
    /// <returns></returns>
    public static String getRandomString(short length) {
        return getRandomString(length, randomStringChars);
    }

    public static String getRandomString(String[] list) {
        return list[getRandomInt(0, list.length)];
    }

    /// <summary>
    /// Gets a random String with only lowercase alphabetic characters.
    /// </summary>
    /// <param name="length"></param>
    /// <returns></returns>
    public static String getRandomLowerAlphaString(short length) {
        return getRandomString(length, randomStringOnlyLowercaseChars);
    }

    /// <summary>
    /// Gets a random integer.
    /// </summary>
    /// <param name="minValue"></param>
    /// <param name="maxValue"></param>
    /// <returns></returns>
    public static int getRandomInt(int minValue, int maxValue) {

        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((maxValue - minValue) + 1) + minValue;

        return randomNum;

    }

    public static int pickRandomIntFromList(Integer... list) {
        return list[getRandomInt(0, list.length - 1)];
    }

    /// <summary>
    /// Gets a random username.
    /// </summary>
    /// <returns></returns>
    public static String getRandomUsername() {
        return String.format("%1$s%2$s", firstNames[getRandomInt(0, firstNames.length - 1)], String.valueOf(getRandomInt(10, 99)));
    }

    /// <summary>
    /// Gets a random user name.
    /// </summary>
    /// <returns></returns>
    public static String getRandomUserName() {
        return String.format("%1$s %2$s", firstNames[getRandomInt(0, firstNames.length - 1)], lastNames[getRandomInt(0, lastNames.length - 1)]);
    }


    /// <summary>
    /// Gets a random first name.
    /// </summary>
    /// <returns></returns>
    public static String getRandomFirstName() {
        return firstNames[getRandomInt(0, firstNames.length - 1)];
    }

    /// <summary>
    /// Gets a random last name.
    /// </summary>
    /// <returns></returns>
    public static String getRandomLastName() {
        return lastNames[getRandomInt(0, lastNames.length - 1)];
    }

    /// <summary>
    /// Gets a random email address.
    /// </summary>
    /// <returns></returns>
    public static String getRandomEmailAddress() {
        String host = emailHosts[getRandomInt(0, emailHosts.length - 1)];

        return String.format("%1$s@%2$s", getRandomUsername().replace(" ", ""), host);
    }

    public static String getRandomMobileNumber() {
        String prefix = "601";
        String part2 = Integer.toString(getRandomInt(11111111, 99999999));

        return String.format("%1$s%2$s", prefix, part2);
    }


    public static String getRandomMobileNumber(String prefix, int length) {
        String number = prefix;

        for (int i = 0; i <= length; i++) {
            prefix += getRandomInt(0, 9);
        }

        return number;
    }

    /// <summary>
    /// Gets a random land line phone number.
    /// </summary>
    /// <returns></returns>
    public static String getRandomLandlineNumber() {
        return String.format("61%1$s%2$s0%3$s%4$s", getRandomInt(1, 9), getRandomInt(1, 9), getRandomInt(1, 9), getRandomInt(1111, 9999));
    }

    /// <summary>
    /// Gets a random boolean value.
    /// </summary>
    /// <returns></returns>
    public static boolean getRandomBoolean() {
        return Math.random() < 0.5;
    }

    /// <summary>
    /// Returns a random date within the years specified
    /// </summary>
    /// <param name="minYear">lowest year to generate date, inclusive</param>
    /// <param name="maxYear">highest year to generate date, inclusive</param>
    /// <returns>LocalDateTime</returns>
//    public static LocalDateTime GetRandomDate(int minYear, int maxYear)
//    {
//        int year = ValueGenerator.getRandomInt(minYear, maxYear);
//
//        int month = ValueGenerator.getRandomInt(1, 12);
//
//        int daysInMonth = LocalDateTime.DaysInMonth(year, month);
//        int day = ValueGenerator.getRandomInt(1, daysInMonth);
//
//        return LocalDateTime.of(year, month, day);
//    }

    /// <summary>
    /// Returns a random date within the year specified
    /// </summary>
    /// <param name="year">year to generate date</param>
    /// <returns>LocalDateTime</returns>
//    public static LocalDateTime GetRandomDate(int year)
//    {
//        return GetRandomDate(year, year);
//    }

    public static String getRandomSubmissionTitle() {
        String verb = verbs[getRandomInt(0, verbs.length - 1)];
        String adjective = adjectives[getRandomInt(0, adjectives.length - 1)];
        String noun = nouns[getRandomInt(0, nouns.length - 1)];
        if (getRandomBoolean()) {
            return String.format("%1$s", noun);
        } else {
            if (getRandomBoolean()) {
                return String.format("%1$s %2$s %3$s", verb, adjective, noun);
            } else {
                if (getRandomBoolean()) {
                    return getRandomUsername();
                } else {
                    return String.format("%1$s %2$s", tags[getRandomInt(0, tags.length - 1)], tags[getRandomInt(0, tags.length - 1)]);
                }
            }
        }
    }

    public static String getRandomVideoFilename() {
        return videoFilenames[getRandomInt(0, videoFilenames.length - 1)];
    }

    public static String getRandomAudioFilename() {
        return audioFilenames[getRandomInt(0, audioFilenames.length - 1)];
    }

    public static String getRandomImageFilename() {
        return imageFilenames[getRandomInt(0, imageFilenames.length - 1)];
    }

    public static String getRandomComment() {
        StringBuilder comment = new StringBuilder();

        if (getRandomBoolean()) {
            if (getRandomBoolean())
                comment.append("lol! ");
            else
                comment.append("LOL! ");
        }
        if (getRandomBoolean()) {
            comment.append(userNameStarts[getRandomInt(0, userNameStarts.length - 1)]);

        }
        if (getRandomBoolean()) {
            comment.append(" ");
            comment.append(userNameEnds[getRandomInt(0, userNameEnds.length - 1)]);
        }
        if (getRandomBoolean()) {
            comment.append(" ");
            comment.append(firstNames[getRandomInt(0, firstNames.length - 1)]);
        }
        if (getRandomBoolean()) {
            comment.append(" ");
            comment.append(nouns[getRandomInt(0, nouns.length - 1)]);
        }
        comment.append(" ");
        comment.append(verbs[getRandomInt(0, verbs.length - 1)]);
        comment.append(" ");
        comment.append(adjectives[getRandomInt(0, adjectives.length - 1)]);
        comment.append(" ");
        comment.append(nouns[getRandomInt(0, nouns.length - 1)]);
        comment.append(" ");
        comment.append(tags[getRandomInt(0, tags.length - 1)]);

        if (getRandomBoolean()) {
            comment.append(" ");
            if (getRandomBoolean()) {
                comment = new StringBuilder();
                if (getRandomBoolean())
                    comment.append("LOL! ");
            }
            comment.append(randomPhrases[getRandomInt(0, randomPhrases.length - 1)]);
        }

        if (getRandomBoolean()) {
            comment.append(" ");
            if (getRandomBoolean()) {
                comment = new StringBuilder();
                if (getRandomBoolean())
                    comment.append("LOL! ");
            }
            comment.append(randomMessageStrings[getRandomInt(0, randomMessageStrings.length - 1)]);
        }

        if (getRandomBoolean())
            comment.append(" asl?");


        return comment.toString();

    }

    /// <summary>
    /// Gets a random Tag.
    /// </summary>
    /// <returns></returns>
    public static String getRandomTag() {
        return getRandomTag(false);
    }


    /// <summary>
    /// Gets a random Tag.
    /// </summary>
    /// <returns></returns>
    public static String getRandomTag(boolean isUnique) {
        String tag = tags[getRandomInt(0, tags.length - 1)];

        if (isUnique)
            tag += UUID.randomUUID().toString();

        return tag;
    }

    /// <summary>
    /// Gets a random Tag of x words.
    /// </summary>
    /// <returns></returns>
    public static String getRandomTagPhrase(int minWords, int maxWords) {
        return getRandomTagPhrase(minWords, maxWords, false);
    }


    /// <summary>
    /// Gets a random Tag of x words. Ensures unique.
    /// </summary>
    /// <returns></returns>
    public static String getRandomTagPhrase(int minWords, int maxWords, boolean isUnique) {
        String phrase = "";
        int wordsInTag = getRandomInt(minWords, maxWords);
        for (int j = 0; j < wordsInTag; j++) {
            phrase += getRandomTag() + " ";
        }
        phrase = phrase.trim();
        if (isUnique)
            phrase += UUID.randomUUID().toString();
        return phrase;
    }

    public static String getRandomMessage() {
        StringBuilder randomMessage = new StringBuilder();
        randomMessage.append(randomMessageStrings[getRandomInt(0, randomMessageStrings.length - 1)]);
        randomMessage.append(" ");
        randomMessage.append(getRandomComment());
        randomMessage.append(" ");
        randomMessage.append(LocalDateTime.now().toString("dd-MM-yyyy HH:mm:ss"));

        return randomMessage.toString();
    }

    public static String getRandomReportName() {
        StringBuilder randomReportName = new StringBuilder();
        randomReportName.append(randomReportPrefixStrings[getRandomInt(0, randomReportPrefixStrings.length - 1)]);
        randomReportName.append(" ");
        randomReportName.append(getRandomTagPhrase(1, 3));
        randomReportName.append(" ");
        randomReportName.append(randomMessageStrings[getRandomInt(0, randomMessageStrings.length - 1)]);
        randomReportName.append(" Report ");
        randomReportName.append(LocalDateTime.now().toString("yyyyMMddHHmmss"));

        return randomReportName.toString();
    }

    public static String getRandomArticleTitle() {
        StringBuilder title = new StringBuilder();
        title.append(firstNames[getRandomInt(0, firstNames.length - 1)]);
        title.append(" ");
        title.append(lastNames[getRandomInt(0, lastNames.length - 1)]);
        title.append(" ");
        title.append(verbs[getRandomInt(0, verbs.length - 1)]);
        title.append(" ");
        title.append(adjectives[getRandomInt(0, adjectives.length - 1)]);
        title.append(" ");
        title.append(nouns[getRandomInt(0, nouns.length - 1)]);
        title.append(" ");
        title.append(tags[getRandomInt(0, tags.length - 1)]);

        return title.toString();
    }

    public static String getRandomArticleText(int numParagraphs) {
        StringBuilder articleText = new StringBuilder();
        articleText.append(randomArticleParagraphs[getRandomInt(0, randomArticleParagraphs.length - 1)]);

        for (int i = 1; i < numParagraphs; i++) {
            articleText.append("\r\n");
            articleText.append(randomArticlePadding[getRandomInt(0, randomArticlePadding.length - 1)]);
        }

        return articleText.toString();
    }


    public static String getRandomGender() {
        if (getRandomBoolean()) {
            return "M";
        } else {
            return "F";
        }
    }

    public static String getRandomSMSMessage() {
        String randomComment = getRandomComment();

        if (randomComment.length() > 160)
            randomComment = randomComment.substring(0, 160);

        return randomComment;
    }

    public static String getRandomShopAddress() {
        return randomShopAddress[getRandomInt(0, randomShopAddress.length - 1)];
    }

    public static String getRandomWebsiteAddress() {
        return String.format("www.%1$s.%2$s",
                nouns[getRandomInt(0, nouns.length - 1)],
                randomTopLevelDomains[getRandomInt(0, randomTopLevelDomains.length - 1)]);
    }

    private static Random m_Rand = new Random();

    public static double getRandomDoubleNumber(double min, double max) {
        double result = (max - min) * m_Rand.nextDouble() + min;
        return round(result, 2);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String getRandomOperateHour() {
        return randomTimeFoodDelivery[getRandomInt(0, randomTimeFoodDelivery.length - 1)];
    }
}
