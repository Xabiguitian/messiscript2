-- Users (password is pa2526 encrypted with Bcrypt)
INSERT INTO User (userName, password, firstName, lastName, email, role) VALUES ('viewer', '$2a$10$v.js2jCaX3xoKvkR6E2pbugMmZDBPlCAz2gA7EOIZhbkvsPFew/5u', 'Test', 'Viewer', 'viewer@test.com', 0);
INSERT INTO User (userName, password, firstName, lastName, email, role) VALUES ('ticketseller', '$2a$10$v.js2jCaX3xoKvkR6E2pbugMmZDBPlCAz2gA7EOIZhbkvsPFew/5u', 'Test', 'Seller', 'seller@test.com', 1);

-- Rooms (one with 9, another with > 10)
INSERT INTO Room (name, capacity) VALUES ('Sala 1', 9);
INSERT INTO Room (name, capacity) VALUES ('Sala 2', 50);

-- Movies
INSERT INTO Movie (title, summary, duration) VALUES ('Avatar: The Way of Water', 'Jake Sully lives...', 192);
INSERT INTO Movie (title, summary, duration) VALUES ('The Super Mario Bros. Movie', 'A plumber named Mario...', 92);

-- Sessions
-- 2 sessions for today. Session 1 at 00:05. Session 2 at 23:55 (in the 9 cap room).
INSERT INTO Session (movieId, roomId, date, price, emptySeats) VALUES (1, 2, DATE_ADD(DATE(NOW()), INTERVAL '0 00:05' DAY_MINUTE), 8.50, 48); -- Session 1, emptySeats will be 48 because 2 purchases (below)
INSERT INTO Session (movieId, roomId, date, price, emptySeats) VALUES (2, 1, DATE_ADD(DATE(NOW()), INTERVAL '0 23:55' DAY_MINUTE), 8.50, 9);  -- Session 2, room 1 (capacity 9)

-- 2 sessions for tomorrow
INSERT INTO Session (movieId, roomId, date, price, emptySeats) VALUES (1, 1, DATE_ADD(DATE(NOW()), INTERVAL '1 17:30' DAY_MINUTE), 8.50, 9);
INSERT INTO Session (movieId, roomId, date, price, emptySeats) VALUES (2, 2, DATE_ADD(DATE(NOW()), INTERVAL '1 19:30' DAY_MINUTE), 7.50, 50);

-- 2 sessions for day 2
INSERT INTO Session (movieId, roomId, date, price, emptySeats) VALUES (1, 1, DATE_ADD(DATE(NOW()), INTERVAL '2 18:00' DAY_MINUTE), 8.50, 9);
INSERT INTO Session (movieId, roomId, date, price, emptySeats) VALUES (2, 2, DATE_ADD(DATE(NOW()), INTERVAL '2 21:00' DAY_MINUTE), 7.50, 50);

-- 2 sessions for day 3
INSERT INTO Session (movieId, roomId, date, price, emptySeats) VALUES (1, 1, DATE_ADD(DATE(NOW()), INTERVAL '3 18:00' DAY_MINUTE), 8.50, 9);
INSERT INTO Session (movieId, roomId, date, price, emptySeats) VALUES (2, 2, DATE_ADD(DATE(NOW()), INTERVAL '3 21:00' DAY_MINUTE), 7.50, 50);

-- 2 sessions for day 4
INSERT INTO Session (movieId, roomId, date, price, emptySeats) VALUES (1, 1, DATE_ADD(DATE(NOW()), INTERVAL '4 18:00' DAY_MINUTE), 8.50, 9);
INSERT INTO Session (movieId, roomId, date, price, emptySeats) VALUES (2, 2, DATE_ADD(DATE(NOW()), INTERVAL '4 21:00' DAY_MINUTE), 7.50, 50);

-- 2 sessions for day 5
INSERT INTO Session (movieId, roomId, date, price, emptySeats) VALUES (1, 1, DATE_ADD(DATE(NOW()), INTERVAL '5 18:00' DAY_MINUTE), 8.50, 9);
INSERT INTO Session (movieId, roomId, date, price, emptySeats) VALUES (2, 2, DATE_ADD(DATE(NOW()), INTERVAL '5 21:00' DAY_MINUTE), 7.50, 50);

-- 2 sessions for day 6
INSERT INTO Session (movieId, roomId, date, price, emptySeats) VALUES (1, 1, DATE_ADD(DATE(NOW()), INTERVAL '6 18:00' DAY_MINUTE), 8.50, 9);
INSERT INTO Session (movieId, roomId, date, price, emptySeats) VALUES (2, 2, DATE_ADD(DATE(NOW()), INTERVAL '6 21:00' DAY_MINUTE), 7.50, 50);

-- 2 compras asociadas a la sesion 1
INSERT INTO Purchase (userId, sessionId, tickets, creditCard, date, delivered) VALUES (1, 1, 1, '1234567812345678', DATE_SUB(NOW(), INTERVAL 2 DAY), false);
INSERT INTO Purchase (userId, sessionId, tickets, creditCard, date, delivered) VALUES (1, 1, 1, '8765432187654321', DATE_SUB(NOW(), INTERVAL 1 DAY), false);

