package com.example.classlog.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class UserClassId implements Serializable {

  private Long userId;

  private Long classId;

  @Override
  public boolean equals(Object o) {
      if (this == o) {
          return true;
      }
      if (o == null || getClass() != o.getClass()) {
          return false;
      }

    UserClassId that = (UserClassId) o;

      if (!userId.equals(that.userId)) {
          return false;
      }
    return classId.equals(that.classId);
  }

  @Override
  public int hashCode() {
    int result = userId.hashCode();
    result = 31 * result + classId.hashCode();
    return result;
  }
}
