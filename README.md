## Dự án WordWaves

## Chạy docker image

```sh
docker run --name WordWaves -d -p 8080:8080 -e DATABASE_URL="um58xagionkhpdqn:rBUo4d0HKr41RuWtkvuH@bsg6l6iytlmclu7q87lo-mysql.services.clever-cloud.com:3306/bsg6l6iytlmclu7q87lo" -e DATABASE_USERNAME="um58xagionkhpdqn" -e DATABASE_PASSWORD="rBUo4d0HKr41RuWtkvuH" -e ACCESS_SIGNER_KEY="6FvxQMx9pcbEFqKFFP4XH748Kg//XkcCtf68+/P7ZRvIXnba3dG7POT/cUCAWV5Q" -e REFRESH_SIGNER_KEY="CAqDB1w3mJFJEb3ifzhGVJou64rhlft96l8FSS+i+0XGHSKb+OMxXbzOeHoLxaFL" -e BREVO_API_KEY="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" -e BREVO_SENDER_EMAIL="kaitoukido0204@gmail.com" quan0204/wordwaves-server:latest
```
