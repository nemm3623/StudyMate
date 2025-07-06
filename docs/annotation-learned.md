
@Transactional
- 데이터베이스 트랜잭션을 관리할때 사용
- 메소드가 정상적으로 실행되면 커밋/ else 롤백
- **주요옵션**
  - readOnly
    - 오직 조회만 실행 -> SELECT만 하는 메소드에 주로 사용
  
  - rollbackFor
    - 특정 예외를 지정해서 롤백
    - 기본적으로 트랜잭션은 런타임만 롤백해주기 때문에 해당 옵션을 사용해 지정
  
  - noRollbackFor
    - 해당 예외가 발생시 롤백 X
  
  - propagation
    - 트랜잭션을 분리

  - isolation
    - 각 트랜잭션에 대한 격리 수준