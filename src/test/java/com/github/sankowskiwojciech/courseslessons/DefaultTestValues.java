package com.github.sankowskiwojciech.courseslessons;

public interface DefaultTestValues {

    //subdomain
    String SUBDOMAIN_NAME_STUB = "ssit";

    //organization
    String ORGANIZATION_ALIAS_STUB = "zslitnr1";
    String ORGANIZATION_NAME_STUB = "Zespół szkół licealnych i technicznych nr 1 w Warszawie";
    String ORGANIZATION_DESCRIPTION_STUB = "Technikum mechatroniczno - informatyczne";
    String ORGANIZATION_EMAIL_ADDRESS_STUB = "info@zslitnr1.pl";
    String ORGANIZATION_PHONE_NUMBER_STUB = "123456789";
    String ORGANIZATION_WEBSITE_URL_STUB = "http://zslitnr1.pl";

    //tutor
    String TUTOR_ALIAS_STUB = "wsankowski";
    String TUTOR_FIRST_NAME_STUB = "Wojciech";
    String TUTOR_LAST_NAME_STUB = "Sankowski";
    String TUTOR_DESCRIPTION_STUB = "Java Developer";
    String TUTOR_EMAIL_ADDRESS_STUB = "sankowski.wojciech@gmail.com";
    String TUTOR_PHONE_NUMBER_STUB = "111222333";

    //user
    String USER_ID_STUB = "user@test.com";

    //tutor
    String STUDENT_FIRST_NAME_STUB = "Marcin";
    String STUDENT_LAST_NAME_STUB = "Baczewski";
    String STUDENT_EMAIL_ADDRESS_STUB = "marcin.b@gmail.com";

    //token
    String TOKEN_VALUE_STUB = "tokenValueStub";
    String RSA_PUBLIC_KEY_STUB = "rsaPublicKey";

    //lesson
    long INDIVIDUAL_LESSON_ID_STUB = 1;
    String LESSON_TITLE_STUB = "Tytul lekcji.";
    String LESSON_DESCRIPTION_STUB = "Opis lekcji.";

    //file
    String FILE_NAME_STUB = "Test file.pdf";
    String FILE_CONTENT_TYPE_STUB = "application/pdf";
    byte[] FILE_CONTENT_STUB = new byte[5];
}
