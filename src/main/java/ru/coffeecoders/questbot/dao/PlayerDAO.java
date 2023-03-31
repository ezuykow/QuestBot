package ru.coffeecoders.questbot.dao;

import org.springframework.stereotype.Component;

/**
 * @author ezuykow
 */
@Component
public class PlayerDAO {
/*
    private static class PlayerMapper implements RowMapper<Player> {

        @Value("${dynamic.tables.players.idColumn}")
        private String tgUserIdColumnName;
        @Value("${dynamic.tables.players.teamNameColumn}")
        private String teamNameColumnName;

        @Override
        public Player mapRow(ResultSet rs, int rowNum) throws SQLException {

            Player player = new Player();
            player.setTgUserId(rs.getLong(tgUserIdColumnName));
            player.setTeamName(rs.getString(teamNameColumnName));

            return player;
        }
    }

    @Value("${dynamic.tables.players.idColumn}")
    private String idColumnName;

    private final JdbcTemplate jdbcTemplate;
    private final PlayerMapper playerMapper;

    public PlayerDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.playerMapper = new PlayerMapper();
    }

    *//**
     * Поиск всех игроков
     * @param tableName имя таблицы, из которой брать игроков
     * @return список игроков
     *//*
    public List<Player> findAll(String tableName) {
        final String statement = String.format("SELECT * FROM %s", tableName);
        return jdbcTemplate.query(statement, playerMapper);
    }

    *//**
     * Поиск игрока по id
     * @param tableName имя таблицы, из которой брать игрока
     * @param id id искомого игрока
     * @return Optional с найденым игроком, либо пустой, в случае, если такого игрока не существует
     *//*
    public Optional<Player> findById(String tableName, long id) {
        final String statement = String.format("SELECT * FROM %s WHERE %s = %d",
                tableName, idColumnName, id);
        return jdbcTemplate.query(statement, playerMapper).stream().findAny();
    }

    *//**
     * Добавление нового игрока в таблицу или редактирование существующего
     * @param tableName имя таблицы, в которую добавлять игрока
     * @param player добавляемый игрок
     * @return {@code true} если добавление прошло успешно, {@code false} если добавление не прошло
     *//*
    public boolean save(String tableName, Player player) {
        final String statement = String.format("INSERT INTO %s VALUES(%d, %s)",
                tableName, player.getTgUserId(), player.getTeamName());
        return jdbcTemplate.update(statement) > 0;
    }*/
}
