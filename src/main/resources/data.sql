-- ORGANIZATIONS
insert into ORGANIZATION(EMAIL_ADDRESS, ALIAS, NAME, DESCRIPTION)
values ('info@zslitnr1.pl', 'zslitnr1', 'Zespół szkół licealnych i technicznych nr 1 w Warszawie', 'Technikum mechatroniczno - informatyczne');
insert into ORGANIZATION(EMAIL_ADDRESS, ALIAS, NAME, DESCRIPTION, PHONE_NUMBER)
values ('not.spam@dancewithme.pl', 'ssit', 'Szkoła śpiewu i tańca', 'Szkoła gdzie się uczy śpiewać i tańczyć', '123456789');

-- TUTORS
insert into TUTOR(EMAIL_ADDRESS, ALIAS, FIRST_NAME, LAST_NAME, DESCRIPTION, PHONE_NUMBER)
values ('sankowski.wojciech@gmail.com', 'wsankowski', 'Wojciech', 'Sankowski', 'Java Developer', '111222333');
insert into TUTOR(EMAIL_ADDRESS, ALIAS, FIRST_NAME, LAST_NAME, DESCRIPTION)
values ('kaminski.bart@dancewithme.pl', 'barkam', 'Bartosz', 'Kamiński', 'Profesjonalny tancerz');

-- PARENTS
insert into PARENT(EMAIL_ADDRESS, FIRST_NAME, LAST_NAME)
values ('kbac@gmail.com', 'Kamil', 'Baczewski');

-- STUDENTS
insert into STUDENT(EMAIL_ADDRESS, FIRST_NAME, LAST_NAME, PARENT_ID)
values ('marcin.b@gmail.com', 'Marcin', 'Baczewski', 'kbac@gmail.com');
insert into STUDENT(EMAIL_ADDRESS, FIRST_NAME, LAST_NAME)
values ('rawskaka@gmail.com', 'Karolina', 'Rawska');
insert into STUDENT(EMAIL_ADDRESS, FIRST_NAME, LAST_NAME)
values ('adamjan@wp.pl', 'Adam', 'Janczewski');
insert into STUDENT(EMAIL_ADDRESS, FIRST_NAME, LAST_NAME)
values ('4rturz', 'Artur', 'Zdun');


-- SUBDOMAIN USER ACCESSES
insert into SUBDOMAIN_USER_ACCESS(SUBDOMAIN_EMAIL_ADDRESS, USER_EMAIL_ADDRESS)
values ('info@zslitnr1.pl', 'sankowski.wojciech@gmail.com');
insert into SUBDOMAIN_USER_ACCESS(SUBDOMAIN_EMAIL_ADDRESS, USER_EMAIL_ADDRESS)
values ('sankowski.wojciech@gmail.com', 'sankowski.wojciech@gmail.com');
insert into SUBDOMAIN_USER_ACCESS(SUBDOMAIN_EMAIL_ADDRESS, USER_EMAIL_ADDRESS)
values ('sankowski.wojciech@gmail.com', 'marcin.b@gmail.com');
insert into SUBDOMAIN_USER_ACCESS(SUBDOMAIN_EMAIL_ADDRESS, USER_EMAIL_ADDRESS)
values ('info@zslitnr1.pl', 'marcin.b@gmail.com');
insert into SUBDOMAIN_USER_ACCESS(SUBDOMAIN_EMAIL_ADDRESS, USER_EMAIL_ADDRESS)
values ('not.spam@dancewithme.pl', 'marcin.b@gmail.com');
insert into SUBDOMAIN_USER_ACCESS(SUBDOMAIN_EMAIL_ADDRESS, USER_EMAIL_ADDRESS)
values ('not.spam@dancewithme.pl', 'kaminski.bart@dancewithme.pl');
insert into SUBDOMAIN_USER_ACCESS(SUBDOMAIN_EMAIL_ADDRESS, USER_EMAIL_ADDRESS)
values ('sankowski.wojciech@gmail.com', 'rawskaka@gmail.com');

-- TOKENS
insert into TOKEN(TOKEN_ID, TOKEN_VALUE, USER_EMAIL_ADDRESS, ACCOUNT_TYPE, RSA_PUBLIC_KEY, CREATION_DATE_TIME, EXPIRATION_DATE_TIME)
values ('token1', 'tokenNotIssuedForProvidedClient', 'sankowski.wojciech@gmail.com', 'TUTOR', 'rsaPublicKey', '2020-07-18T18:21:14.144', '2220-07-18T18:21:14.144');
insert into TOKEN(TOKEN_ID, TOKEN_VALUE, USER_EMAIL_ADDRESS, ACCOUNT_TYPE, RSA_PUBLIC_KEY, CREATION_DATE_TIME, EXPIRATION_DATE_TIME, REVOCATION_DATE_TIME)
values ('token2', 'tokenIsRevoked', 'sankowski.wojciech@gmail.com', 'TUTOR', 'rsaPublicKey', '2020-07-18T18:21:14.144', '2220-07-18T18:21:14.144', '2020-07-18T18:21:14.144');
insert into TOKEN(TOKEN_ID, TOKEN_VALUE, USER_EMAIL_ADDRESS, ACCOUNT_TYPE, RSA_PUBLIC_KEY, CREATION_DATE_TIME, EXPIRATION_DATE_TIME)
values ('token3', 'tokenIsExpired', 'sankowski.wojciech@gmail.com', 'TUTOR', 'rsaPublicKey', '2020-07-18T18:21:14.144', '2020-07-19T18:21:14.144');
insert into TOKEN(TOKEN_ID, TOKEN_VALUE, USER_EMAIL_ADDRESS, ACCOUNT_TYPE, RSA_PUBLIC_KEY, CREATION_DATE_TIME, EXPIRATION_DATE_TIME)
values ('token4', 'tokenIsValid', 'sankowski.wojciech@gmail.com', 'TUTOR', 'rsaPublicKey', '2020-07-18T18:21:14.144', '2220-07-18T18:21:14.144');
insert into TOKEN(TOKEN_ID, TOKEN_VALUE, USER_EMAIL_ADDRESS, ACCOUNT_TYPE, RSA_PUBLIC_KEY, CREATION_DATE_TIME, EXPIRATION_DATE_TIME) values ('ff40d81e-63b3-4012-84fb-613d6246bab3', 'eyJhbGciOiJFUzUxMiJ9.eyJqdGkiOiIxN2RjNmI3MC01ZWFiLTQxMzYtYmJlZS1mYTFjNjk1ZjAzMGIiLCJzdWIiOiJzYW5rb3dza2kud29qY2llY2hAZ21haWwuY29tIiwiaWF0IjoxNTk2NTM3NjU5LCJleHAiOjE1OTY1NDEyNTl9.ASq8hv-kiTnSiQnvrYJwhUztVyYV0_Rgk7TbvZiO-pooqmc9zDkODOT-8tabYl2XG1Ge8q2_OQfEUzq4_9FhQbFBAcZUheuJ5iP3koEz0-8R8e8rlCsI6Nyv_xx95EwIJJNJwEE7wGLQvPk8-hgBnu0MLEiEL9N9H_o6M8QQD6Qdu6zQ', 'sankowski.wojciech@gmail.com', 'TUTOR', 'MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQALwq2GPPSda8X5s6IcGvKTV8a+W/AtNBY1BI320wLFvoRg7tQC/iJhZ9aJo+sBzp2by4un4RehnRaBFg3nqqwOoIAJ0Oi4UaQYB+RpLAPLn9Wgkg+mlLlULwBWzqLb9qLEpA9OOq9ZbPQh/5narHYuuiD8/NDgIp9KuAN8o4MILqs9q0=', '2020-07-18T18:21:14.144', '2220-07-18T18:21:14.144');
insert into TOKEN(TOKEN_ID, TOKEN_VALUE, USER_EMAIL_ADDRESS, ACCOUNT_TYPE, RSA_PUBLIC_KEY, CREATION_DATE_TIME, EXPIRATION_DATE_TIME) values ('ff40d81e-63b3-4012-84fb-ghvb34568321', 'tokenValueStub', 'sankowski.wojciech@gmail.com', 'TUTOR', 'MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQALwq2GPPSda8X5s6IcGvKTV8a+W/AtNBY1BI320wLFvoRg7tQC/iJhZ9aJo+sBzp2by4un4RehnRaBFg3nqqwOoIAJ0Oi4UaQYB+RpLAPLn9Wgkg+mlLlULwBWzqLb9qLEpA9OOq9ZbPQh/5narHYuuiD8/NDgIp9KuAN8o4MILqs9q0=', '2020-07-18T18:21:14.144', '2220-07-18T18:21:14.144');

-- INDIVIDUAL LESSONS
insert into INDIVIDUAL_LESSON(TITLE, START_DATE_OF_LESSON, END_DATE_OF_LESSON, DESCRIPTION, ORGANIZATION_ID, TUTOR_ID, STUDENT_ID, CREATION_DATE_TIME)
values ('Lekcja 1', '2020-07-18T18:00:00', '2020-07-18T20:00:00', 'Opis lekcji', 'info@zslitnr1.pl', 'sankowski.wojciech@gmail.com', 'marcin.b@gmail.com', '2020-07-17T18:00:00');
insert into INDIVIDUAL_LESSON(TITLE, START_DATE_OF_LESSON, END_DATE_OF_LESSON, DESCRIPTION, ORGANIZATION_ID, TUTOR_ID, STUDENT_ID, CREATION_DATE_TIME)
values ('Lekcja 2', '2020-07-20T18:00:00', '2020-07-20T20:00:00', 'Opis lekcji', 'info@zslitnr1.pl', 'sankowski.wojciech@gmail.com', 'marcin.b@gmail.com', '2020-07-19T18:00:00');
insert into INDIVIDUAL_LESSON(TITLE, START_DATE_OF_LESSON, END_DATE_OF_LESSON, DESCRIPTION, TUTOR_ID, STUDENT_ID, CREATION_DATE_TIME)
values ('Lekcja indywidualna bardzo', '2020-07-20T14:00:00', '2020-07-20T15:00:00', 'Opis lekcji', 'sankowski.wojciech@gmail.com', 'marcin.b@gmail.com', '2020-07-20T12:00:00');
insert into INDIVIDUAL_LESSON(TITLE, START_DATE_OF_LESSON, END_DATE_OF_LESSON, DESCRIPTION, TUTOR_ID, STUDENT_ID, CREATION_DATE_TIME)
values ('Lekcja indywidualna nr 2', '2020-07-21T14:00:00', '2020-07-21T16:00:00', 'Opis lekcji', 'sankowski.wojciech@gmail.com', 'rawskaka@gmail.com', '2020-07-21T10:00:00');
insert into INDIVIDUAL_LESSON(TITLE, START_DATE_OF_LESSON, END_DATE_OF_LESSON, DESCRIPTION, ORGANIZATION_ID, TUTOR_ID, STUDENT_ID, CREATION_DATE_TIME)
values ('Lekcja 3', '2020-07-21T18:00:00', '2020-07-21T20:30:00', 'Opis lekcji', 'not.spam@dancewithme.pl', 'kaminski.bart@dancewithme.pl', 'marcin.b@gmail.com', '2020-07-15T18:00:00');
insert into INDIVIDUAL_LESSON(TITLE, START_DATE_OF_LESSON, END_DATE_OF_LESSON, DESCRIPTION, ORGANIZATION_ID, TUTOR_ID, STUDENT_ID, CREATION_DATE_TIME)
values ('Angular routing część 1', '2020-09-18T18:00:00', '2020-09-18T19:00:00', 'Kurs dotyczący routingu w Angularze 9+', 'info@zslitnr1.pl', 'sankowski.wojciech@gmail.com', 'rawskaka@gmail.com', '2020-07-17T18:00:00');
insert into INDIVIDUAL_LESSON(TITLE, START_DATE_OF_LESSON, END_DATE_OF_LESSON, DESCRIPTION, ORGANIZATION_ID, TUTOR_ID, STUDENT_ID, CREATION_DATE_TIME)
values ('Angular routing część 2', '2020-09-19T18:00:00', '2020-09-19T20:00:00', 'Kontynuacja kursu dotyczący routingu w Angularze 9+', 'info@zslitnr1.pl', 'sankowski.wojciech@gmail.com', 'rawskaka@gmail.com', '2020-07-17T18:00:00');
insert into INDIVIDUAL_LESSON(TITLE, START_DATE_OF_LESSON, END_DATE_OF_LESSON, DESCRIPTION, ORGANIZATION_ID, TUTOR_ID, STUDENT_ID, CREATION_DATE_TIME)
values ('Angular template-driven forms', '2020-09-18T18:00:00', '2020-09-18T21:00:00', 'Template-driven forms w pigułce.', 'info@zslitnr1.pl', 'sankowski.wojciech@gmail.com', 'rawskaka@gmail.com', '2020-07-17T18:00:00');