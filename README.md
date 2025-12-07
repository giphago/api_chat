# api_chat
================================================<br />
사용된 기술 스택<br 
<img width="300" height="400" alt="image" src="https://github.com/user-attachments/assets/36e29f10-aa4c-4fa8-a166-530e3dec1b73" />
================================================<br />
DeepL API 연동 모듈 개발<br />
데이터 모델: DeepL 응답을 담기 위한 DeepLTranslateResponse 및 DeepLTranslation 데이터 클래스 정의.<br />
Retrofit Service: DeepLTranslateApi 인터페이스를 정의하고, @Header, @POST, @Field 어노테이션을 사용하여 폼 데이터(Form-Encoded) 방식으로 DeepL API에 요청하는 로직 구현.<br />
Retrofit Client: RetrofitClient 싱글턴 객체를 생성하여 BASE_URL, API 키, 로깅 인터셉터가 포함된 OkHttpClient를 설정하고, DeepLTranslateApi 서비스 인스턴스 생성.<br />
================================================<br />

