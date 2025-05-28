INSERT INTO meetings (
     title, book_title, book_author, book_cover,
     book_category, start_date, max_members, is_active, created_at, updated_at
 ) VALUES (
     '함께하는 심리학 독서모임',
     '자기 이해의 심리학',
     '이민규',
     'https://example.com/psychology.jpg',
     '심리학',
     '2025-06-10 10:00:00',
     15,
     true,
     NOW(),
     NOW()
 );

 INSERT INTO meetings (
     title, book_title, book_author, book_cover,
     book_category, start_date, max_members, is_active, created_at, updated_at
 ) VALUES (
     '철학하는 밤',
     '니체의 말',
     '프리드리히 니체',
     'https://example.com/nietzsche.jpg',
     '철학',
     '2025-06-05 19:00:00',
     10,
     true,
     NOW(),
     NOW()
 );
