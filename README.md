## Dự án WordWaves

## Chạy docker image

docker run --name WordWaves -d -p 8080:8080 
-e DATABASE_URL=ugy3xdlg4yzsgnbc:AJMQxWffY78sikU7JJjx@bv50k19rodxkhj0lbdvb-mysql.services.clever-cloud.com:3306/bv50k19rodxkhj0lbdvb 
-e DATABASE_USERNAME=ugy3xdlg4yzsgnbc 
-e DATABASE_PASSWORD=AJMQxWffY78sikU7JJjx 
-e ACCESS_SIGNER_KEY=6FvxQMx9pcbEFqKFFP4XH748Kg//XkcCtf68+/P7ZRvIXnba3dG7POT/cUCAWV5Q
-e REFRESH_SIGNER_KEY=CAqDB1w3mJFJEb3ifzhGVJou64rhlft96l8FSS+i+0XGHSKb+OMxXbzOeHoLxaFL
-e BREVO_API_KEY=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
-e BREVO_SENDER_EMAIL=kaitoukido0204@gmail.com
quan0204/wordwaves:latest