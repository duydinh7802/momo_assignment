# Momo Software Engineer Assignment Test - Dinh Ba Duy

## Build & Run

```bash
# Tạo thư mục cần thiết
make folder

# Compile toàn bộ source
make build

# Chạy chương trình
make run

# Chạy unit test
make test

# Xóa file build
make clean
```

---

## Danh sách lệnh

| Lệnh | Mô tả |
|------|-------|
| `CASH_IN <amount>` | Nạp tiền vào tài khoản |
| `CREATE_BILL <TYPE> <AMOUNT> <DD/MM/YYYY> <PROVIDER>` | Tạo hóa đơn mới |
| `UPDATE_BILL <id> <TYPE> <AMOUNT> <DD/MM/YYYY> <PROVIDER>` | Cập nhật hóa đơn |
| `DELETE_BILL <id>` | Xóa hóa đơn |
| `LIST_BILL` | Xem danh sách tất cả hóa đơn |
| `PAY <id> [id2 ...]` | Thanh toán một hoặc nhiều hóa đơn |
| `DUE_DATE` | Xem hóa đơn chưa thanh toán, sắp xếp theo ngày đến hạn |
| `SCHEDULE <id> <DD/MM/YYYY>` | Đặt lịch thanh toán tự động |
| `LIST_PAYMENT` | Xem lịch sử giao dịch |
| `SEARCH_BILL_BY_PROVIDER <PROVIDER>` | Tìm hóa đơn theo nhà cung cấp |
| `EXIT` | Thoát chương trình |

---