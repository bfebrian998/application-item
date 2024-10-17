package com.application.storage.storage.Entity;

import com.application.storage.storage.dto.response.AdditionalInfoDto;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "barang")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
@TypeDefs({
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class),
})
public class StorageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "barang_seq")
    @SequenceGenerator(name = "barang_seq", sequenceName = "barang_seq", allocationSize = 1)
    @Column(name = "id_barang")
    private Long idBarang;

    @Column(name = "nama_barang", nullable = false, length = 255)
    private String namaBarang;

    @Column(name = "jumlah_stok_barang", nullable = false)
    private Integer jumlahStokBarang;

    @Column(name = "nomor_seri_barang", nullable = false, unique = true, length = 100)
    private String nomorSeriBarang;


    @Type(type = "jsonb")
    @Column(name = "additional_info", columnDefinition = "jsonb")
    private AdditionalInfoDto additionalInfo;

    @Column(name = "gambar_barang")
    private byte[] gambarBarang;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;
}