# About
이 라이브러리는 리틀팍스 앱에서 사용되는 다양한 뷰 모듈을 포함하고 있습니다.<br>
애니메이션, 다이얼로그, 비디오 플레이어, 프로그래스뷰 등의 커스텀 뷰를 제공하며,<br>
프로젝트에서 해당 라이브러리를 사용하여 보다 효율적이고 간편한 UI 개발이 가능합니다.

이 라이브러리는 Android 앱 개발에 필수적인 기능들을 포함하고 있어, <br>
개발자분들이 앱 UI 개발에 많은 시간을 절약할 수 있습니다. <br>
또한, 이 라이브러리는 리틀팍스의 경험을 바탕으로 제작되었기 때문에,<br>
안정성과 성능면에서도 높은 품질을 보장합니다.

감사합니다!

# Getting Started
안드로이드 스튜디오나 gradle을 사용하시는 분께서는<br>
아래 dependencies를 build.gradle에 추가해주시면 바로 사용해보실수 있습니다.
```groovy
implementation 'com.github.only2433:LittlefoxViewLibrary:1.1.7'
```

# Usage
#### 주요 사용 하는 모듈 
- ViewAnimator
    ```
    View 애니메이션을 쉽게 할 수 있게 도와주는 Builder 패턴의 모듈
    ```
- Material Dialog
    ```
    Material 스타일의 지속적으로 로딩해서 화면에 표시하는 다이얼로그
    ```
- FadeAnimationController
    ```
    뷰가 Fade In/Out 될때, 동시에 여러 애니메이션을 해야하는 상황에, 
    사용한다. Queue에 애니메이션을 적재하고, In/Out될때 pull 하여
    애니메이션 여러개를 동작 시키는 모듈
    ```    
- SwipeDisableViewPager
    ```
    Swipe를 Disable 하기 위해 만듣 ViewPager
    ```
- ProgressiveMediaPlayer
    ```
    URL 영상을 다운로드 하면서 플레이 하기 위해 만든 영상 플레이어 모듈
    ```
- CircleProgressView
    ```
    Circle 로 된 프로그래스 바. 현재의 퍼센트 상태를 나타내기 위해 사용
    보통 다운로드 될때, 사용 된다.
    ```  
- FixedSpeedScroller
    ```
    ViewPager의 이동 속도를 조절하기 위해 만든 Scroller
    사용자가 주입한 시간에 따라 애니메이션 속도가 바뀐다.
    ```    
    
    
# License
본 프로젝트는 MIT 라이선스를 따릅니다.<br>
자세한 내용은 [LICENSE](https://github.com/only2433/LittlefoxViewLibrary/blob/master/License.md) 파일을 참고해주세요



