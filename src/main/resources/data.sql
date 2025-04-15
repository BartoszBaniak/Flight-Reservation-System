CREATE TABLE IF NOT EXISTS airports (
                                        airport_id SERIAL PRIMARY KEY,
                                        airport_code VARCHAR(10) UNIQUE NOT NULL,
                                        airport_city VARCHAR(100) NOT NULL
);

INSERT INTO airports (airport_code, airport_city) VALUES
                                                      ('LHR', 'Londyn'),
                                                      ('CDG', 'Paryż'),
                                                      ('AMS', 'Amsterdam'),
                                                      ('FRA', 'Frankfurt'),
                                                      ('IST', 'Stambuł'),
                                                      ('MAD', 'Madryt'),
                                                      ('BCN', 'Barcelona'),
                                                      ('MUC', 'Monachium'),
                                                      ('FCO', 'Rzym'),
                                                      ('ZRH', 'Zurych'),
                                                      ('VIE', 'Wiedeń'),
                                                      ('CPH', 'Kopenhaga'),
                                                      ('DUB', 'Dublin'),
                                                      ('BRU', 'Bruksela'),
                                                      ('ARN', 'Sztokholm'),
                                                      ('OSL', 'Oslo'),
                                                      ('WAW', 'Warszawa'),
                                                      ('ATH', 'Ateny'),
                                                      ('LIS', 'Lizbona'),
                                                      ('PRG', 'Praga');
