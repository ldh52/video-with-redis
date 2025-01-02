INSERT INTO users (id, name) VALUES ('user', 'Foo Bar');

INSERT INTO channel (id, title, description, published_at, thumbnail_url, subscriber_count, video_count, comment_count, content_owner_id) VALUES ('channel1', 'channel1', 'channel1', '2024-01-01 00:00:00', 'https://example.com', 100, 100, 100, 'user');

INSERT INTO video (id, title, description, channel_id, thumbnail_url, file_url, published_at) VALUES ('video1', 'video1', 'video1', 'channel1', 'https://example.com', 'https://exmaple.com/video1.mp4', '2024-01-01 00:00:00');
INSERT INTO video (id, title, description, channel_id, thumbnail_url, file_url, published_at) VALUES ('video2', 'video2', 'video2', 'channel1', 'https://example.com', 'https://exmaple.com/video2.mp4', '2024-01-01 00:00:00');