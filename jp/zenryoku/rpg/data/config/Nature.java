package jp.zenryoku.rpg.data.config;

import java.util.List;
import lombok.Data;

@Data
public class Nature extends StoryConfig {
  /** 気候リスト */
  private List<Climate> climateList;
  /** 食物連鎖 */
  private String food_chain;
}
