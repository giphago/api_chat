# api_chat
================================================<br />
**사용된 기술 스택**<br />
<img width="300" height="400" alt="image" src="https://github.com/user-attachments/assets/36e29f10-aa4c-4fa8-a166-530e3dec1b73" /><br />
================================================<br />
**DeepL API 연동 모듈 개발**<br />

**데이터 모델**: DeepL 응답을 담기 위한 DeepLTranslateResponse 및 DeepLTranslation 데이터 클래스 정의.<br />
**Retrofit Service**: DeepLTranslateApi 인터페이스를 정의하고, @Header, @POST, @Field 어노테이션을 사용하여 폼 데이터(Form-Encoded) 방식으로 DeepL API에 요청하는 로직 구현.<br />
**Retrofit Client**: RetrofitClient 싱글턴 객체를 생성하여 BASE_URL, API 키, 로깅 인터셉터가 포함된 OkHttpClient를 설정하고, DeepLTranslateApi 서비스 인스턴스 생성.<br />
================================================<br />
**채팅 UI 및 데이터 관리 구현**

**메시지 모델**: Message(text: String, isUser: Boolean) 데이터 클래스를 정의하여 메시지의 내용과 발신자(사용자 또는 챗봇)를 구분.<br />
**UI 레이아웃 (activity\_main.xml)**: 메시지 목록을 위한 RecyclerView와 하단의 사용자 입력창 (EditText), 전송 버튼(Button) 배치.<br />
**아이템 레이아웃 (item\_message\_user.xml, item\_message\_chatbot.xml)**: 사용자 메시지는 오른쪽 정렬/다른 배경, 챗봇 메시지는 왼쪽 정렬/다른 배경을 사용하도록 분리 구현.<br />
================================================<br />
**메인 로직 및 비동기 처리**

**MainActivity 연결**: RecyclerView와 MessageAdapter를 초기화하고, 버튼 클릭 리스너를 설정.
**비동기 번역**: sendMessage() 함수 내에서 사용자 메시지를 리스트에 추가하고, translateText() 함수를 코루틴(Dispatchers.IO)으로 호출하여 API 통신.
================================================<br />
**(network/api.kt)에서 api 키를 입력하여야함.** <br />
================================================<br />

<br /><img width="300" height="400" alt="image" src="https://github.com/user-attachments/assets/b9fb645f-7aa6-4611-8f1b-221692fd3f5d" /><br />
