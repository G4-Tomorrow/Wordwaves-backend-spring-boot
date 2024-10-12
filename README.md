## Phần Backend của dự án WordWaves

## Chạy docker image

```sh
docker run --name WordWaves -d -p 8080:8080 -e DATABASE_URL="avnadmin:AVNS_NaOkIhMfoLS45jVy0F4@wordwaves-wordwaves.f.aivencloud.com:21917/defaultdb?ssl-mode=REQUIRED" -e DATABASE_USERNAME="avnadmin" -e DATABASE_PASSWORD="AVNS_NaOkIhMfoLS45jVy0F4" -e ACCESS_SIGNER_KEY="6FvxQMx9pcbEFqKFFP4XH748Kg//XkcCtf68+/P7ZRvIXnba3dG7POT/cUCAWV5Q" -e REFRESH_SIGNER_KEY="CAqDB1w3mJFJEb3ifzhGVJou64rhlft96l8FSS+i+0XGHSKb+OMxXbzOeHoLxaFL" -e BREVO_API_KEY="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" -e BREVO_SENDER_EMAIL="kaitoukido0204@gmail.com" -e ACCESS_TOKEN_EXPIRATION=300 -e REFRESH_TOKEN_EXPIRATION=900 quan0204/wordwaves-server:latest
```
## Link server
```sh
https://backend-production-c2cb.up.railway.app/wordwaves/<path>
```
## Thông tin Database
- Host:
```plaintext
wordwaves-wordwaves.f.aivencloud.com
```
- User:
```plaintext
avnadmin
```
- Password:
```plaintext
AVNS_NaOkIhMfoLS45jVy0F4
```

