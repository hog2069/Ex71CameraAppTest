package com.hog2020.ex71cameraapptest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv=findViewById(R.id.iv);
    }

    public void clickphoto(View view) {
        //Camera 앱을 실행시키는 intent 생성 - 외부 저장소 사용에대한 퍼미션필요[단 실제 외부저장소에 캡쳐된 사진 파일은 저장하지않음]
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //결과를 받으야 하므로 결과를 받을 수 있는 액티비티 실행
        startActivityForResult(intent,100); //startActivity() 를 사용하면 에러
    }

    //startActivityForResult() 메소드로 실행된 액티비티가 종료되면
    //자동으로 발동하는 메소드

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //100번 Intent 가 맞고 결과를 가지고 들어왔는지 확인
        if (requestCode==100 && resultCode==RESULT_OK){
            //디바이스 마다 프로그래밍으로 실행시킨 카메라 앱의 사진캡쳐방식이 다름
            //예전 폰들은 캡쳐한 사진을 파일로 디바이스에 자동저장 그 결과를 파일의 경로정보(Uri)로 전달해줌
            //요즘 대다수의 폰들은 사진을 파일로 저장하지 않고 사진데이터(byte[])값을 결과로 돌려줌

            //결과를 가지고 들어온 Intent 객체(이 메소드의 3번째 파라미터:data) 에게 결과 얻어오기
            Uri uri =data.getData();

            if (uri != null){//파일로 저장되는 방식의 디바이스
                Toast.makeText(this, "uri 사진정보 취득", Toast.LENGTH_SHORT).show();
                //iv.setImageURI(uri);//메모리 문제를 신경써야 해서 편하게 이미지라이브러리 사용
                Glide.with(this).load(uri).into(iv);

            }else{//uri가 없다는 것은 파일로 저장되지 않고 byte[]로 사진데이터만 가져온 경우[요즘 대부분] 이미지객체인 Bitmap 으로 전달해주는
                Toast.makeText(this, "Bitmap 객체로 사진정보 취득", Toast.LENGTH_SHORT).show();

                Bundle bundle = data.getExtras();//돌아온 인텐트가 가지고 온 추가데이터 얻기
                Bitmap bm = (Bitmap) bundle.get("data");
                Glide.with(this).load(bm).into(iv);

                //사진데이터의 해상도가 안 좋음 -Bitmap 객체 정보는 사실 캡쳐된 사진의 섬네일 이미지임
                //해상도가 좋게 하고 싶다면 파일형태도 저장되도록 프로그래밍 해야 함
                //파일로 저장하지 않으면 나중에 사준데이터를 서버애 업로드도 할 수 없음
                //다음 예제에서 파일로 저장되도록 프로그래밍 해보기
            }
       }
    }
}