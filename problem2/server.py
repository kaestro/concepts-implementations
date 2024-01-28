# server.py
import socket
import os
import time

# 수정된 파일을 찾아서 반환하는 함수를 정의합니다.
# 이 함수는 디렉토리와 파일 수정 시간을 저장하는 딕셔너리를 인자로 받습니다.
# 딕셔너리에는 파일 이름과 수정 시간이 저장되어 있습니다.
# 딕셔너리에 파일 이름이 없거나 수정 시간이 다르면 해당 파일을 반환합니다.
# 딕셔너리에 파일 이름이 있고 수정 시간이 같으면 빈 리스트를 반환합니다.
# 딕셔너리에 파일 이름이 있고 수정 시간이 다르면 파일 이름을 반환합니다.
# 딕셔너리에 파일 이름이 없으면 파일 이름을 반환합니다.
# 딕셔너리의 값을 수정하는 side effect가 있으므로 주의해야 합니다.
def get_modified_files(directory, file_mod_times):
    modified_files = []
    for filename in os.listdir(directory):
        filepath = os.path.join(directory, filename)
        mod_time = os.path.getmtime(filepath)

        if filename not in file_mod_times or file_mod_times[filename] < mod_time:
            file_mod_times[filename] = mod_time
            modified_files.append(filename)
    return modified_files

def server_program():
    host = '0.0.0.0'
    port = 12345

    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)  # socket 객체를 생성합니다.
    server_socket.bind((host, port))  # 서버의 호스트와 포트를 지정합니다.
    server_socket.listen()  # 클라이언트의 연결을 기다립니다.
    print('Server is listening...')

    conn, address = server_socket.accept()  # 클라이언트의 연결을 수락합니다.
    print('Connection from:', address)

    conn.send('Successfully connected'.encode())  # 클라이언트에 메시지를 보냅니다.

    directory_to_watch = os.path.join(os.path.dirname(os.path.realpath(__file__)), 'dummy_server_folder')
    file_mod_times = {} # 파일의 수정 시간을 저장할 딕셔너리를 생성합니다

    while True:
        modified_files = get_modified_files(directory_to_watch, file_mod_times)
        for filename in modified_files:
            filepath = os.path.join(directory_to_watch, filename)
            with open(filepath, 'rb') as file_to_send:
                message = f"File {filename} has been modified."
                conn.send(message.encode())

                filesize = os.path.getsize(filepath)
                conn.send(f"Size {filesize}".encode())

                for data in file_to_send:
                    conn.sendall(data)

        conn.send('keep-alive'.encode())
        time.sleep(10)

    conn.close()  # 연결을 닫습니다.

if __name__ == '__main__':
    server_program()