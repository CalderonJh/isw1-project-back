CREATE SCHEMA IF NOT EXISTS app;

SET search_path TO app;

drop table if exists app.season_pass_holder;
drop table if exists app.season_pass_type;
drop table if exists app.season_pass_offer;
drop table if exists app.ticket_purchase;
drop table if exists app.ticket_type;
drop table if exists app.ticket_offer;
drop table if exists app.club_admin;
drop table if exists app.subscription;
drop table if exists app.match;
drop table if exists app.stand;
drop table if exists app.stadium;
drop table if exists app.club;
drop table if exists app.user_role;
drop table if exists app.user;
drop table if exists app.role;
drop table if exists app.person;
drop table if exists app.identity_document;
