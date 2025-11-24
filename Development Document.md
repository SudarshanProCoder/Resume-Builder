## Section - 01: Development Steps

**Step 1:** Setup the backend project, create spring boot project (https://start.spring.io) - Web, MongoDB, Lombok, Validation

**Step 2:** Configure the mongoDB database

**Step 3:** Create Application health Check API endpoint

**Step 4:** Create Register API `/api/auth/register`

- Create Request and Response objects
- Create Document object to map the mongodb collection
- Create Mongodb repository
- Create Service
- Create Controller
- Test the API endpoint

**Step 5:** Send Verification email to registered email address

- Add the spring email dependency
- Create a account in Brevo and the SMTP server details
- Add the SMTP details into application.properties file
- Create email service to send emails
- Update the AuthService to call email service method


**Step 6:** Verify the token

- Create a new API endpoint '/api/auth/verify-email'
- Create a finder method in UserRepository
- Create a service method to verify the token
- Update the property isEmailVerified to true