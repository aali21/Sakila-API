import os
from dotenv import load_dotenv

def generate_app_yaml(template_path, output_path):
    # Load environment variables from .env file
    load_dotenv()

    with open(template_path, 'r') as template_file:
        content = template_file.read()

    db_name = os.getenv('DB_NAME')
    connection_name = os.getenv('CONNECTION_NAME')

    content = content.replace('${DB_NAME}', db_name)
    content = content.replace('${CONNECTION_NAME}', connection_name)

    # Write the modified content to the output file
    with open(output_path, 'w') as output_file:
        output_file.write(content)


if __name__=="__main__":
    template_path = 'src\\main\\appengine\\app.template.yaml'
    output_path = 'src\\main\\appengine\\app.yaml'

    generate_app_yaml(template_path, output_path)
    print(f'{output_path} has been generated.')