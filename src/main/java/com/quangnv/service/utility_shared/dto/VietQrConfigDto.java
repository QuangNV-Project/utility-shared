package com.quangnv.service.utility_shared.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VietQrConfigDto {
    // Mã BIN ngân hàng, được quy định bởi ngân hàng nhà nước
    String bankId;
    // Số tài khoản người nhận tại ngân hàng thụ hưởng.
    String accountNo;
    // compact2	540x640	Bao gồm : Mã QR, các logo , thông tin chuyển khoản
    // compact	540x540	QR kèm logo VietQR, Napas, ngân hàng
    // qr_only	480x480	Trả về ảnh QR đơn giản, chỉ bao gồm QR
    // print	600x776	Bao gồm : Mã QR, các logo và đầy đủ thông tin chuyển khoản
    String template;
    // quy định tên người thụ hưởng hiển thị lên file ảnh VietQR
    String accountName;
    //nội dung chuyển khoản.
    String description;
}
