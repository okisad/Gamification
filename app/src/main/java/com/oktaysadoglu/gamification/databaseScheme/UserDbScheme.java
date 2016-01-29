package com.oktaysadoglu.gamification.databaseScheme;

/**
 * Created by oktaysadoglu on 10/12/15.
 */
public class UserDbScheme {

    public static final class UserWordTable{

        public static final String NAME="user_word_table";

        public static final class Columns{

            public static final String BASE_WORD_ID = "base_word_id";

            public static final String NUMBER_OF_NOT_KNOW = "not_know_number";

            public static final String Date = "date";

        }

    }

    public static final class LastSeenWordTable{

        public static final String NAME = "last_seen_word_table";

        public static final class Columns{

            public static final String LEVEL = "level";

            public static final String LAST_BASE_WORD_ID = "last_base_word_id";

        }

    }

    public static final class LogTable{

        public static final String NAME = "log_table";

        public static final class Columns{

            public static final String LOG_NAME = "log_name";

            public static final String LOG_DESCRIPTION = "log_description";

            public static final String DATE = "date";

        }

    }
}
