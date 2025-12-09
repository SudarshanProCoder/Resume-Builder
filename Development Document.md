## Section - 01: Auth Service Development Steps

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

**Step 7:** Add the cloudinary dependency

**Step 8:** Add the cloudinary configurstions

**Step 9:** Create FileUploadService

**Step 10:** Create API endpoint to `/api/auth/upload-image` upload profile image

**Step 11:** Test the API endpoint proper working or not

## Section - 02: Login Feature Development Steps

**Step 1:** Add the spring security dependency

**Step 2:** Configure the security filter chain

**Step 3:** Encode the password while registering new account

**Step 4:** Create Login Request

**Step 5:** Create API endpoint `/api/auth/login` for Login

## Section - 03: Resend Email Verification Feature Development Steps

## Section - 04: User Profile Feature Development Steps

## Section - 05: Build Resume Feature Development Steps

## Section - 06: Build Resume Templates Feature Development Steps

## Section - 07: Razorpay Integration Feature Development Steps

## Section - 07: Send Email PDF Feature Development Steps
