package com.sg.pokemonproject.Dao;

import com.sg.pokemonproject.Entity.Ability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AbilityDaoDB implements AbilityDao {

    @Autowired
    JdbcTemplate jdbc;


    @Override
    public Ability addAbility(Ability ability) {
        final String sql = "Insert into Ability(Name, AP, Attack) Values(?,?,?)";
        jdbc.update(sql, ability.getName(), ability.getAP(), ability.getAttack());


        int newId = jdbc.queryForObject("Select Last_Insert_Id()", Integer.class);
        ability.setId(newId);

        return ability;
    }

    @Override
    public Ability getAbilityById(int id) {
        try {
            final String sql = "Select * From Ability Where Abilityid = ?";
            return jdbc.queryForObject(sql, new AbilityMapper(), id);
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public void updateAbility(Ability ability) {
        final String sql = "Update Ability Set Name = ?, AP = ?, Attack = ? Where Abilityid = ?";
        jdbc.update(sql, ability.getName(), ability.getAP(), ability.getAttack(), ability.getId());
    }

    @Override
    public void deleteAbilityById(int id) {
        final String sql1 = "Delete From Poke_Ability Where Abilityid = ?";
        jdbc.update(sql1, id);

        final String sql = "Delete From Ability Where Abilityid = ?";
        jdbc.update(sql, id);
    }

    @Override
    public List<Ability> getAll() {
        final String sql = "Select * From Ability";


        return jdbc.query(sql, new AbilityMapper());
    }

    public static final class AbilityMapper implements RowMapper<Ability> {

        @Override
        public Ability mapRow(ResultSet rs, int index) throws SQLException {
            Ability ability = new Ability();
            ability.setId(rs.getInt("Abilityid"));
            ability.setName(rs.getString("Name"));
            ability.setAP(rs.getInt("AP"));
            ability.setAttack(Integer.parseInt(rs.getString("Attack")));

            return ability;
        }
    }
}