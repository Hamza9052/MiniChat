package com.example.test.token

import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

object AccessToken {

    private const val FIREBASE_MESSAGE_SCOPE = "https://www.googleapis.com/auth/firebase.messaging"

    suspend fun getToken(): String? {
        return withContext(Dispatchers.IO){
            try {
                val json = "{\n" +
                        "  \"type\": \"service_account\",\n" +
                        "  \"project_id\": \"test-6d005\",\n" +
                        "  \"private_key_id\": \"1d96fb2a63f8dcd1c8f82d3bf919a5bbee1ade25\",\n" +
                        "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCezFKq5umCTmGX\\nrpnho5p3z67aDkObAAeZxvTn5tmUgCVX0BpsB02zlt1jxuwcnFw6Ep+MA+nuOqiB\\nnTDq959J5VxXBW75e0cOt2bUk+dmeuE+ieWsAlSCedrKJd+lC8yUJC0R6CBK/oIw\\nxB+J/uk/1ISy0SNilJ2cS989yrL0PfdLv8bSlJKUtuUs/I7dgXD+MjgaDkeyQ6gt\\npwChaqZGPj8gRO9nVssKopsJhLL1u8nD4bAWTCQe454WECsTwvtr1BCb0WMJBcK1\\neq/RD28DZXVtXnM3wWpK6tN4kfflpUK4FxyULkj+S/GLRi+Y6unORHxb2PJWktKF\\nBtq89DkdAgMBAAECggEAG4xz3P1BTl9ds+xQ+uGglx97BI5L8TRwnR90dlTIs2gF\\n0nWiV9JuivTlxH0Kki4YG+UGM8qsBy269P15spGx2yaEvf4N0g8udSGhH8jm8JLp\\nx6kxpIUk9xu/tqkaYu4omBysivo7u/bqTB6CIDUObwnl2YXJvzzsGOTavkQPhqqB\\nKyAQ2TY44nFkmRtJs3zltfRVcgjklOS+cQQ9Jv1qez23XbU16JVzdDzQNdmn/KtT\\nnXVTn948EJsVliMt9T4HW3JXtoM8vWP7U3JiCNtwd1O5axK+FuxHQWvnErs+wHq4\\nFPXFNb9PFOf965DnQ4TUw40f4EQllM76mTLRC9+1ywKBgQDbXEfhR7ruclS5PjQ2\\nP5srsb3iCVZoNmVWOsil5pHIAwzl0JH98yBJcDpthMPZ+SGscRfLOc+IzfPXFJn8\\nY4cevQoupGZwkQuxyQEv6seMY4OO7YofhYtN38q9Ur5nX8tYYKcGqjd8s9lwc+cP\\nQlvUk3YUFqhlwx7I3ZqiEvcjSwKBgQC5Um8PP2svU61GKhIgGxDbIED3onKqkFy9\\n9xYZ5mowiKa/MxzbjNZ2N4UuGa3+abK1EO32n1AaxhMikEbt+mRPe6wC1/mJpusi\\nA+jSLiR1ry2SilGDrMEOOogLdfGc74KPzJUnrc+lWAjFcD9qc9nAjFoT+4+BD1Nm\\n/d1zYehsNwKBgQCJM7PlzTa3c51Ya/a+Q5RrhPWuTobUEcnaYFQlqAxgnL/Rcl4O\\nB6aXKHHzFEpnqLKOKAJo41TSTXcJfOI/gYT/DeAzgjJsj9xPq9hpPyDmx0t+90Yg\\nJygVYjdtqTZcdAc81N9DhO3Furvy4RuRaMEFOeJt+CoF1EAKBMl2PASF9wKBgQCv\\n39+uZM1gGNV0pTCWb5lLnAyL9wtDe8XIMtoysk1MyiMViXh1k+9kOcSWv6A0tjL3\\nS53KU989yYUXZD0sVFkmZYQVHsqfLF4x5evqrAfF9/4T+Fjr0eNVkY7S8ZffDB6o\\nQcu74GQT0rxgQ+qrKE8bQRerpy3FmbnZIEwt2FkveQKBgEd5hkRX8CNZ1TvWt4/f\\ngZCQhqS7TIUgtFwLTvFr9H4VB5ZhQu/0niMyH5z/b2AOb7vlxuLSqJ12HFKjFjG1\\nT/H4bZT2EmZkupuAPSGfoReSib+XJM6s4mlHEpJtu0Hgemqt4gQFf8IL68O8756q\\nnR0kQvN6BJnVtX+68dVcjp/z\\n-----END PRIVATE KEY-----\\n\",\n" +
                        "  \"client_email\": \"firebase-adminsdk-l8emv@test-6d005.iam.gserviceaccount.com\",\n" +
                        "  \"client_id\": \"115517371844440990989\",\n" +
                        "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                        "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                        "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                        "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-l8emv%40test-6d005.iam.gserviceaccount.com\",\n" +
                        "  \"universe_domain\": \"googleapis.com\"\n" +
                        "}\n"

                // Convert JSON to a stream
                val stream = ByteArrayInputStream(json.toByteArray(StandardCharsets.UTF_8))

                // Load Google credentials
                val googleCredentials = GoogleCredentials.fromStream(stream)
                    .createScoped(listOf(FIREBASE_MESSAGE_SCOPE))

                // Refresh token
                googleCredentials.refreshIfExpired()
                val token = googleCredentials.accessToken.tokenValue

                Log.d("AccessToken", "Successfully retrieved token: $token")
                token
            } catch (e: Exception) {
                Log.e("AccessToken", "Failed to retrieve token", e)
                null
            }
        }
    }
}