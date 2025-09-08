package com.hem.cleartouchmedia;

import java.util.Locale;

public class Constant {

    private static String DOMAIN_URL = "http://155.138.158.13/"; //domain or admin panel url
    public static String BASE_URL = DOMAIN_URL + "api/updateScreenRegiToken";  //api url
    //public static String imgURL = "https://dev.cleartouchmedia.com/images/";
    public static String imgURL = "https://demo.cleartouchmedia.com/images/";
    public static String FOLDER_NAME = "ClearTouchMedia";
    public static int DELAY_SECONDS = 30 * 60 * 1000;//30*60000=1800000
//    public static int DELAY_SECONDS = 1000;
    /////// PARAMETERS  ///////
    public static String AUTHORIZATION = "Authorization";
    public static String JWT_KEY = "set_your_strong_jwt_secret_key";
    public static String DEVELOPER_KEY = "AIzaSyAyN98uyLS_RBB_bNMu5cym6pknB9C1XBM";
    public static String accessKey = "access_key";
    public static String accessKeyValue = "6808";
    public static String name = "name";
    public static String email = "email";
    public static String mobile = "mobile";
    public static String type = "type";
    public static String limit = "limit";
    public static String fcmId = "fcm_id";
    public static String userId = "user_id";
    public static String PROFILE = "profile";
    public static String userSignUp = "user_signup";
    public static String status = "status";
    public static String ipAddress = "ip_address";
    public static String getCategories = "get_categories";
    public static String getRandomQuestion = "get_random_questions";
    public static String getQuestionByLevel = "get_questions_by_level";
    public static String getPageQuestion = "get_Page_questions";
    public static String getDailyQuiz = "get_daily_quiz";
    public static String getSubCategory = "get_subcategory_by_maincategory";
    public static String getVideo = "get_video";
    public static String getScreenRegistrationToken = "screenRegiToken";
    public static String getUpdateScreenRegistrationToken = "updateScreenRegiToken";
    public static String device_id = "device_id";
    public static String device_token = "device_token";
    public static String device_model = "device_model";
    public static String device_os = "device_os";
    public static String device_manufacturer = "device_manufacturer";
    public static String apk_version = "apk_version";
    public static String sdk_version = "sdk_version";
    public static String private_ip = "private_ip";
    public static String public_ip = "public_ip";
    public static String screen_id = "screen_id";
    public static String mac_address = "mac_address";

    public static String category = "category";
    public static String Level = "level";
    public static String getPrivacy = "privacy_policy_settings";
    public static String getTerms = "get_terms_conditions_settings";
    public static String get_about_us = "get_about_us";
    public static String get_quiz_message = "get_quiz_message";
    public static String get_quiz_battle_message = "get_quiz_battle_message";
    public static String upload_profile_image = "upload_profile_image";

    public static String image = "image";
    public static String updateFcmId = "update_fcm_id";
    public static String updateProfile = "update_profile";
    public static String getMontlyLeaderboard = "get_monthly_leaderboard";
    public static String setMonthlyLeaderboard = "set_monthly_leaderboard";
    public static String SET_LEVEL_DATA = "set_level_data";
    public static String GET_LEVEL_DATA = "get_level_data";
    public static String NO_OF_CATE = "no_of";
    public static String NO_OF_QUES = "no_of_que";
    public static String GET_USER_BY_ID = "get_user_by_id";
    public static String GET_GLOBAL_LB = "get_global_leaderboard";
    public static String GET_TODAYS_LB = "get_datewise_leaderboard";

    public static String FROM = "from";
    public static String TO = "to";
    public static String getInstructions = "get_instructions";
    public static String get_questions_by_type = "get_questions_by_type";
    public static String GET_CATE_BY_LANG = "get_categories_by_language";
    public static String SET_BATTLE_STATISTICS = "set_battle_statistics";
    public static String GET_BATTLE_STATISTICS = "get_battle_statistics";
    public static String IS_DRAWN = "is_drawn";
    public static String SET_USER_STATISTICS = "set_users_statistics";
    public static String GET_USER_STATISTICS = "get_users_statistics";
    public static String GET_SYSTEM_CONFIG = "get_system_configurations";
    public static String GET_LANGUAGES = "get_languages";
    public static String GET_NOTIFICATIONS = "get_notifications";
    public static String LANGUAGE = "language";
    public static String SetBookmark = "set_bookmark";
    public static String Question_Id = "question_id";
    public static String GetBookmark = "get_bookmark";
    public static String LANGUAGE_ID = "language_id";
    public static String RATIO = "ratio";
    public static String CORRECT_ANSWERS = "correct_answers";
    public static String QUESTION_ANSWERED = "questions_answered";
    public static String AUTH_ID = "firebase_id";
    public static String QUE_TYPE = "question_type";
    public static String LIVE_CONTEST = "live_contest";
    public static String UPCOMING_CONTEST = "upcoming_contest";
    public static String PAST_CONTEST = "past_contest";
    public static String STARTDATE = "start_date";
    public static String ENDDATE = "end_date";
    public static String ENTRY = "entry";
    public static String DESCRIPTION = "description";
    public static String PARTICIPANTS = "participants";
    public static String DATECREATED = "date_created";
    public static String POINTS = "points";
    public static String TOP_WINNERS = "top_winner";
    public static String TOPUSERS = "top_users";
    public static String DATE = "date";
    public static String RANK = "user_rank";
    public static String SCORE = "score";
    public static String COINS = "coins";
    public static String ERROR = "error";
    public static String DATA = "data";
    public static String ID = "id";
    public static String OFFSET = "offset";
    public static String LIMIT = "limit";
    public static String KEY_APP_LINK = "app_link";
    public static String KEY_LANGUAGE_MODE = "language_mode";
    public static String KEY_ANSWER_MODE = "answer_mode";
    public static String KEY_OPTION_E_MODE = "option_e_mode";
    public static String KEY_APP_VERSION = "app_version";
    public static String KEY_SHARE_TEXT = "shareapp_text";
    public static String CATEGORY_NAME = "category_name";
    public static String IMAGE = "image";
    public static String MAX_LEVEL = "maxlevel";
    public static String MAIN_CATE_ID = "maincat_id";
    public static String SUB_CATE_NAME = "subcategory_name";
    public static String REFER_CODE = "refer_code";
    public static String FRIENDS_CODE = "friends_code";
    public static String STRONG_CATE = "strong_category";
    public static String WEAK_CATE = "weak_category";
    public static String RATIO_1 = "ratio1";
    public static String RATIO_2 = "ratio2";
    public static String QUESTION = "question";
    public static String OPTION_A = "optiona";
    public static String OPTION_B = "optionb";
    public static String OPTION_C = "optionc";
    public static String OPTION_D = "optiond";
    public static String OPTION_E = "optione";
    public static String LEVEL = "level";
    public static String NOTE = "note";
    public static String USER_ID1 = "user_id1";
    public static String USER_ID2 = "user_id2";
    public static String GAME_ROOM_KEY = "match_id";
    public static String DE_ACTIVE = "0";
    public static String REFER_COIN = "refer_coin";
    public static String EARN_COIN = "earn_coin";
    public static String REWARD_COIN = "reward_coin";
    public static String WINNER_ID = "winner_id";
    public static String OPPONENT_NAME = "opponent_name";
    public static String OPPONENT_PROFILE = "opponent_profile";
    public static String MY_STATUS = "mystatus";
    public static String GLOBAL_SCORE = "all_time_score";
    public static String GLOBAL_RANK = "all_time_rank";
    public static String KEY_MORE_APP = "more_apps";
    public static String TRUE_FALSE = "2";
    public static String DailyQuizText = "daily_quiz_mode";
    public static String ContestText = "contest_mode";
    public static String ForceUpdateText = "force_update";
    /*-----------fireBase database column names for battle---------*/

    public static String STATUS = "status";
    public static String LEFT_BATTLE = "leftBattle";
    public static String IS_AVAIL = "isAvail";
    public static String LANG_ID = "langId";
    public static String DB_GAME_ROOM_NEW = "BattleRoom";
    //public static String DB_GAME_ROOM_NEW = "game_room";
    public static String OPPONENT_ID = "opponentID";
    public static String MATCHING_ID = "matchingID";
    public static String USER_NAME = "name";
    public static String USER_ID = "userID";
    public static String NAME = "name";
    public static String QUESTIONS = "Questions";
    public static String RIGHT_ANS = "rightAns";
    public static String SEL_ANS = "userSelect";
    public static String TOTAL = "total";
    public static String DESTROY_GAME_KEY = "destroy_match";
    public static String REFER_POINTS = "50";// refer points , you can change here
    public static int MAX_MINUTES = 60; //max minutes for self challenge quiz
    public static int MAX_QUESTION_PER_BATTLE = 10; // max question per level
    public static int RANDOM_QUE_LIMIT=10;
    public static int TOTAL_COINS;
    public static long LeftTime;
    public static int TotalLevel;
    public static int CATE_ID;
    public static int SUB_CAT_ID;
    public static String cate_name;
    public static String verificationCode;
    public static String LANGUAGE_MODE;
    public static String OPTION_E_MODE;
    public static String SHARE_APP_TEXT;
    public static String REFER_COIN_VALUE;
    public static String EARN_COIN_VALUE;
    public static int REWARD_COIN_VALUE = 0;
    public static String VERSION_CODE;
    public static String REQUIRED_VERSION;
    public static String DAILYQUIZON;
    public static String CONTESTON;
    public static String FORCEUPDATE;
    public static String QUICK_ANSWER_ENABLE = "0";
    public static long TAKE_TIME = 0;
    public static long CHALLENGE_TIME = 0;
    public static String PROGRESS_COLOR = "#CCF3F5";  // change progress color of circle timer
    public static String PROGRESS_BG_COLOR = "#306c83";
    public static String AUD_PROGRESS_COLOR = "#CCF3F5"; //audience progress color
    public static String AUD_PROGRESS_BG_COLOR = "#306c83";
    public static int PROGRESS_TEXT_SIZE = 13; // progress text size
    public static int PROGRESS_STROKE_WIDTH = 4; // stroke width
    public static int RESULT_PROGRESS_STROKE_WIDTH = 7;
    public static int RESULT_PROGRESS_TEXT_SIZE = 20;
    public static int PAGE_LIMIT = 50;
    public static String BATTLE_PAGE_LIMIT = "5";
    public static final String PREF_TEXTSIZE = "fontSizePref";
    public static final String D_LANG_ID = "-1";
    public static final String TEXTSIZE_MAX = "30"; //maximum text size of play area question
    public static final String TEXTSIZE_MIN = "18";//minimum default text for play area question


    /*-----------Tournament Api---------*/

    public static String Transecation_Process = "transaction_process";
    public static String Details = "detail";
    public static String Transecation_Type = "transaction_type";
    public static String Pay_amount = "pay_amount";
    public static String GETCONTEST = "get_contest";
    public static String GETDATAKEY = "1";
    public static String GETLEADERBOARD = "get_contest_leaderboard";
    public static String CONTEST_ID = "contest_id";
    public static int LIFELINESCORE = 4;
    public static int TIMER_MILLI_SECOND = 26000;
    public static String CONTEST_UPDATE_SCORE = "contest_update_score";
    public static String QUESTION_ATTEND = "questions_attended";
    public static String ANSWER = "answer";
    public static String GET_QUESTION_BY_CONTEST = "get_questions_by_contest";
    public static String ADDPOINT = "add_points";
    public static String TYPE = "type";
    public static int score1 = 3;
    public static int score2 = 2;
    public static int score3 = 1;

    /// you can increase or decrease time
    public static int CIRCULAR_MAX_PROGRESS = 25; // max here we set 25 second for each question, you can increase or decrease time here
    public static int TIME_PER_QUESTION = 25000;  //here we set 25 second to milliseconds
    public static int COUNT_DOWN_TIMER = 1000; //here we set 1 second
    public static int OPPONENT_SEARCH_TIME = 11000; // time for search opponent for battle
    public static int FOR_CORRECT_ANS = 4; // mark for correct answer
    public static int PENALTY = 2;// minus mark for incorrect

    //////------------give coin to user , when level completed----------//////

//    public static int PASSING_PER = 30;  //count level complete when user give >30 percent correct answer
    public static int PASSING_PER = 80;  //count level complete when user give >80 percent correct answer

    public static int giveOneCoin = 1;  //give  coin when user give 30 to 40 percent correct answer
    public static int giveTwoCoins = 2; //give  coins when user give 40 to 50 percent correct answer
    public static int giveThreeCoins = 3; //give  coin when user give 50 to 60 percent correct answer
    public static int giveFourCoins = 4;  //give  coin when user give > 60  percent correct answer

    public static String APP_LINK = "http://play.google.com/store/apps/details?id=";
    public static String MORE_APP_URL = "https://play.google.com/store/apps/developer?id=";


    /////////*******TextToSpeech Language Change ******////////
    public static Locale ttsLanguage = Locale.ENGLISH;

    /////////******* Battled Friend List field ******////////
    public static String OPPONENT_FRIEND_ID = "opponent_id";
    public static String OPPONENT_FRIEND_NAME = "opponent_name";
    public static String OPPONENT_FRIEND_PROFILE = "opponent_profile";
    public static String OPPONENT_FRIEND_STATUS = "mystatus";

    /////////******* Video List field ******////////
    public static String VIDEO_ID = "video_id";
    public static String VIDEO_TITLE = "video_title";
    public static String VIDEO_DETAIL = "video_detial";
    public static String VIDEO_URL = "video_url";
    public static String VIDEO_STATUS = "status";
    public static String VIDEO_CREATED_DATE = "created_date";

    /////////******* Ads List field ******////////
    public static String ADS_ID = "ads_id";
    public static String ADS_TITLE = "ads_title";
    public static String ADS_DETAIL = "ads_detail";
    public static String ADS_URL = "ads_image_url";
    public static String ADS_TYPE = "ads_type";
    public static String ADS_CATEGORY = "ads_category";
    public static String ADS_STATUS = "status";
    public static String ADS_CREATED_DATE = "created_date";

    public static String MEDIA_IMAGE = "media_image";
    public static String MEDIA_VIDEO = "media_video";
    public static String MEDIA_AUDIO = "media_audio";
    public static String STORAGE_ACCEPTED = "storage_accepted";
    public static String NETWORK_STATE = "network_state";
    public static String READ_PHONE_STATE = "read_phone_state";

}