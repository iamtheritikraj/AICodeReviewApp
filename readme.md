<h1>AI Code Review App</h1>
<p>The AI Code Review App is a backend service built using Java that allows users to log in via GitHub OAuth, retrieve their repositories, and review code files using AI models like ChatGPT or Claude. The application leverages GitHub's API to fetch repository information and file contents, which are then analyzed by AI models for code review. The results of the review are presented to the user in a concise and informative manner.</p>
<h3>Features</h3>
<ul>
<li>GitHub OAuth Login: Secure authentication using GitHub OAuth, allowing users to log in using their GitHub credentials.</li>
<li>Repository Listing: Retrieve a list of repositories associated with the authenticated GitHub user.</li>
<li>File Content Retrieval: Fetch the contents of files from the user's repositories using GitHub API.</li>
<li>AI Code Review: Send code files to ChatGPT for automated code review and analysis.</li>
<li>Results Display: Display AI-generated review feedback to the user.</li>
</ul>
<h3>Technologies Used</h3>
<ul>
<li>Java: The primary programming language used for building the backend.</li>
<li>Spring Boot: Framework for building the RESTful API and handling dependencies.</li>
<li>OAuth 2.0: For secure authentication with GitHub.</li>
<li>GitHub API: To interact with GitHub repositories and fetch file contents.</li>
<li>AI Models: Integration with AI models like ChatGPT for code review.</li>
<li>Maven: Build and dependency management tool.</li>

<h3>Usage</h3>
<li>Log in with GitHub: Navigate to the /login endpoint, which redirects to GitHub for authentication. Once authenticated, you'll be redirected back to the app with a GitHub access token.</li>

<li>Fetch Repositories: Use the /repos endpoint to list all repositories associated with the authenticated user.</li>

<li>Select a File to Review: Choose a repository and a file to review. Use the /repos/{owner}/{repo}/contents/{path} endpoint to fetch the file content.</li>

<li>Send for Review: Send the file content to the AI model using the /code_review endpoint. The AI will analyze the code and provide feedback.</li>

<li>View Results: The AI's review will be returned and displayed to the user, offering insights and suggestions.</li>
