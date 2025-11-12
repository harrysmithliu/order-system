import re
from typing import List

log_text = """

2025-11-12T12:58:18.895-05:00 DEBUG 22528 --- [nio-8080-exec-2] org.hibernate.SQL                        : /* SELECT new com.harry.order.model.vo.OrderSummaryVO(o.id, o.orderNo, u.id, u.username, p.id, p.productName, o.quantity, o.totalAmount, o.status, o.createTime, o.updateTime) FROM com.harry.order.model.po.Order o JOIN o.user u JOIN o.product p WHERE (:status IS NULL OR o.status = :status) AND (:userId IS NULL OR u.id = :userId) AND (:productId IS NULL OR p.id = :productId) AND (:createdAfter IS NULL OR o.createTime >= :createdAfter) AND (:createdBefore IS NULL OR o.createTime < :createdBefore) AND (:keyword IS NULL OR o.orderNo LIKE CONCAT('%', :keyword, '%') OR p.productName LIKE CONCAT('%', :keyword, '%') OR u.username LIKE CONCAT('%', :keyword, '%')) order by o.createTime desc */ select o1_0.id,o1_0.order_no,o1_0.user_id,u1_0.username,o1_0.product_id,p1_0.product_name,o1_0.quantity,o1_0.total_amount,o1_0.status,o1_0.create_time,o1_0.update_time from t_order o1_0 join t_user u1_0 on u1_0.id=o1_0.user_id join t_product p1_0 on p1_0.id=o1_0.product_id where (? is null or o1_0.status=?) and (? is null or o1_0.user_id=?) and (? is null or o1_0.product_id=?) and (? is null or o1_0.create_time>=?) and (? is null or o1_0.create_time<?) and (? is null or o1_0.order_no like concat('%',?,'%') escape '' or p1_0.product_name like concat('%',?,'%') escape '' or u1_0.username like concat('%',?,'%') escape '') order by o1_0.create_time desc limit ?
2025-11-12T12:58:18.897-05:00 TRACE 22528 --- [nio-8080-exec-2] org.hibernate.orm.jdbc.bind              : binding parameter (1:INTEGER) <- [null]
2025-11-12T12:58:18.898-05:00 TRACE 22528 --- [nio-8080-exec-2] org.hibernate.orm.jdbc.bind              : binding parameter (2:INTEGER) <- [null]
2025-11-12T12:58:18.898-05:00 TRACE 22528 --- [nio-8080-exec-2] org.hibernate.orm.jdbc.bind              : binding parameter (3:BIGINT) <- [1]
2025-11-12T12:58:18.898-05:00 TRACE 22528 --- [nio-8080-exec-2] org.hibernate.orm.jdbc.bind              : binding parameter (4:BIGINT) <- [1]
2025-11-12T12:58:18.898-05:00 TRACE 22528 --- [nio-8080-exec-2] org.hibernate.orm.jdbc.bind              : binding parameter (5:BIGINT) <- [null]
2025-11-12T12:58:18.898-05:00 TRACE 22528 --- [nio-8080-exec-2] org.hibernate.orm.jdbc.bind              : binding parameter (6:BIGINT) <- [null]
2025-11-12T12:58:18.898-05:00 TRACE 22528 --- [nio-8080-exec-2] org.hibernate.orm.jdbc.bind              : binding parameter (7:TIMESTAMP) <- [null]
2025-11-12T12:58:18.898-05:00 TRACE 22528 --- [nio-8080-exec-2] org.hibernate.orm.jdbc.bind              : binding parameter (8:TIMESTAMP) <- [null]
2025-11-12T12:58:18.898-05:00 TRACE 22528 --- [nio-8080-exec-2] org.hibernate.orm.jdbc.bind              : binding parameter (9:TIMESTAMP) <- [null]
2025-11-12T12:58:18.898-05:00 TRACE 22528 --- [nio-8080-exec-2] org.hibernate.orm.jdbc.bind              : binding parameter (10:TIMESTAMP) <- [null]
2025-11-12T12:58:18.898-05:00 TRACE 22528 --- [nio-8080-exec-2] org.hibernate.orm.jdbc.bind              : binding parameter (11:JAVA_OBJECT) <- [null]
2025-11-12T12:58:18.898-05:00 TRACE 22528 --- [nio-8080-exec-2] org.hibernate.orm.jdbc.bind              : binding parameter (12:JAVA_OBJECT) <- [null]
2025-11-12T12:58:18.898-05:00 TRACE 22528 --- [nio-8080-exec-2] org.hibernate.orm.jdbc.bind              : binding parameter (13:JAVA_OBJECT) <- [null]
2025-11-12T12:58:18.898-05:00 TRACE 22528 --- [nio-8080-exec-2] org.hibernate.orm.jdbc.bind              : binding parameter (14:JAVA_OBJECT) <- [null]
2025-11-12T12:58:18.898-05:00 TRACE 22528 --- [nio-8080-exec-2] org.hibernate.orm.jdbc.bind              : binding parameter (15:INTEGER) <- [4]

"""

# 去掉注释块
log_text = re.sub(r"/\*[\s\S]*?\*/", "", log_text)
# 提取SQL模板
sql_match = re.search(r"(?is)(select[\s\S]*?)(?=202\d{2}-\d{2}-\d{2}|\Z)", log_text)
sql = sql_match.group(1).strip() if sql_match else ""

# 提取绑定参数
params: List[str] = re.findall(r"binding parameter.*?<- \[(.*?)\]", log_text)

# 参数格式化
def format_param(p: str) -> str:
    p = p.strip()
    if p.lower() == "null":
        return "NULL"
    if re.match(r"^-?\d+(\.\d+)?$", p):  # 数值类型
        return p
    # 默认加引号
    return f"'{p}'"

# 依次替换
for p in params:
    sql = sql.replace("?", format_param(p), 1)

# 输出结果
print("\n-- Final executable SQL for DBeaver --\n")
print(sql.strip() + ";")

