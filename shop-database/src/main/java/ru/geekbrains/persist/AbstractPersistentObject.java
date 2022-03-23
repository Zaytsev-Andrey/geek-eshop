package ru.geekbrains.persist;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class AbstractPersistentObject {
	
	@Id
//    @GeneratedValue(generator = "UUID")
//    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
//	@GeneratedValue(generator = "uuid-generator")
//	@GenericGenerator(name = "uuid-generator", strategy = "ru.geekbrains.persist.UUIDGenerator")
    @Column(name = "id", columnDefinition = "BINARY(16)", updatable = false, nullable = false)    
    private UUID id = UUIDGenerator.generate();

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof AbstractPersistentObject))
			return false;
		AbstractPersistentObject other = (AbstractPersistentObject) obj;
		return Objects.equals(id, other.getId());
	}

	@Override
	public String toString() {
		return "AbstractPersistentObject [id=" + id + "]";
	}
	
}