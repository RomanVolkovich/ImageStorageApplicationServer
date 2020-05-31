INSERT INTO tbl_album
VALUES (1, 'описание альбома 1', 'альбом первый')
ON CONFLICT DO NOTHING;

INSERT INTO tbl_album
VALUES (2, 'описание второго альбома', 'второй альбом')
ON CONFLICT DO NOTHING;
