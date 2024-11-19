package com.example.loginscreen.token

import android.util.JsonToken
import com.google.auth.oauth2.GoogleCredentials
import kotlinx.serialization.json.Json
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

object AccessToken {
    private val firebaseMessageScope = "https://www.googleapis.com/auth/firebase.messaging"
    fun getToken(): String? {
        try {
            val json:String = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"test-6d005\",\n" +
                    "  \"private_key_id\": \"ad5353ae4a5d7d1484713e46aba7c93ad7eb78bf\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCl6fJCcwjuLCGz\\nVlkVt5bXYOzbVS9kZVlg9v0mxKothpQCPXMjdzHWwoCBoibYf32YgZZUOhQ1bh24\\nUjMUxIsB2tt9EEZuplk0wcZjcEEAQY1xHUSN9Wnt6VLEdMmPn9uXsSUDG9S2c91j\\niUsEodo/sRW60vDE7415o7DnblfFDYE0xUe5oEm4rNkXlcV2oKcFzHO8mmLEhCP+\\nm/8Tcmw97Eh65ZZ7H5PLmg7klX0a2ut45CfG1OCw4hi08o2oEusUGy+MSfHWAG17\\nat9kiF56GSxgfFRL8g5LV5A14WU2N+StKWiYdPDXNu/9IV2zNKxOsE8ENS2qIbmy\\nsQV/SknnAgMBAAECggEAMC/EFpyGaXYTy5w2f2I0wxT5dbJF82ikDs9tMsdHjbVa\\nY1QpjpFdvjv7SqsnkRVmCtcILCo6GFKE+zWUdGGo5b8EpEwHyLczjWkCD3tYxZRE\\nWiff80Llox1y1m/84mPRSTs/vqlknCEGmZQUX6IKWxk5cGs3VlskwcFS7cRUZXxQ\\nr9owL71lTIah+za4yEeH2xxCBpQ0VEoIrm7r51SNKRuwaSSQWHbWAODpQWygLIpd\\nE03K3EJTQn6Zaxv4Y1iF8vKEMKr7Po59J7b8bF/na2PiENFl+Wf9augbK6GmDqd/\\nWpptWRDia+091v0tXePzyHWBTIAvL0YL3z/xdSqAoQKBgQDasR5QgSE0QDuBjPCs\\nNpMt52rVnKoOmpsDqVEoK6YBwkb6ETXW9pI0CVj+qxVEaeSHvPDWRmfPr9pIUH6M\\nXGA8+5ZkoBONy/aJD6f8QmNNv9VDQYGf0U8H4bo56cs5NVTAkhMPh5CknAY64tqR\\nylCw0gS73k0Fx3w3PaZt7vGOnwKBgQDCN92ETuHYCJ+g85+H0cnYvKDDBO3Ju86k\\n943JgZopXVGhFwMWvPiPq5DYPifxjdMoIUSo0N2eiWlaEQv+E7VaQgt9HTvoA1gl\\nYT3lRFswemp27KGTwj44+JcmNHVDziJobbKiE8s1Kuu2y1BJg8kWaFgOUdA4P1Mg\\n9PoDOgAnuQKBgQDO2IYGuQxR4osmDsu452mwGlmHL8X9SChkfype52H9+eauMccN\\nymwgwdKmFeVorwvTg1b8JN2dqAO7MD9xvRHdvEzUYj/8dzV5EBDgXV6VtGMfWP8k\\nzZGFr/A3RLXOJ25BMijLIHsj5ttRqS34hTZq+Eo385xAYh1QFoSrKDzbmwKBgBFp\\n2N6oA+/OtiFENqWC1cos/2z84CUaRajc2UWb7SqQR2VeHtTHFLIs+8unpwUtZiVO\\n9kz8jSJNx7hflues9LGnAkixelGPGzQex+HqgxKRp0IKbWJGJDYPc93lDi6KKOOc\\nEkwd4jZeJsw45nXw7vKnYF3IUBb69oUZ8eCgLqthAoGBAK+ERhutZ6lksmtq+6u+\\nflErlFumB39DMV4s6wRWCx6tGe19SRnVkmQIFd+jNw2OZvsX/7kA9c2cS7+Wvopw\\nvYnpTArlMAvineEZ+1mw0PBUX+QDRV+8ZXCIvMOBJjdFf+yDbranoyi8EikVSotJ\\nzGsx6Q7nzYnpNkGYNb8AqgwB\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-l8emv@test-6d005.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"115517371844440990989\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-l8emv%40test-6d005.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}\n"
            val stream = ByteArrayInputStream(json.toByteArray(StandardCharsets.UTF_8))

            val googleCredential = GoogleCredentials.fromStream(stream)
                .createScoped(arrayListOf(firebaseMessageScope))

            googleCredential.refresh()

            return googleCredential.accessToken.tokenValue


        }catch (e: Exception){
            return null


        }
    }
}