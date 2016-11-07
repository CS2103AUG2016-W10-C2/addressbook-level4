import random
from lxml import etree
from xml.dom import minidom

every_week = '604800000'
exercises = ['Run {} rounds', 'Swim {} laps', 'Do {} push ups']
fruits = ['banana', 'apple', 'pineapple', 'grapes']
modules = ['CS2103T', 'CS2101', 'CS4224', 'CS2105']
output_file = 'SampleData.xml'
date_template = "2016-11-{}T"
start_day = 1
end_day = 18
yesterday = 6

def prettify(rough_string):
    reparsed = minidom.parseString(rough_string)
    return reparsed.toprettyxml(indent="  ")

def create_elem(tag, content):
	output = etree.Element(tag)
	output.text = content
	return output

def create_task(day, is_marked):
	rand_title = random.choice(exercises).format(day)
	display_date = str(day) if day > 9 else '0' + str(day)
	datetime = date_template.format(display_date) + "09:00"
	desc = "Or just sleep..."
	tag = "Healthy"
	lastModifiedTime = date_template.format(display_date) + "09:00"

	output = etree.Element('entries')
	output.append(create_elem('title', rand_title))
	output.append(create_elem('description', desc))
	output.append(create_elem('end', datetime))
	output.append(create_elem('tagged', tag))
	output.append(create_elem('isMarked', is_marked))
	output.append(create_elem('lastModified', lastModifiedTime))
	output.append(create_elem('recursion', '0'))

	return output

def create_floating_task(day, is_marked):
	rand_title = "Buy {} {}".format(day, random.choice(fruits))
	tag1 = "NTUC"
	tag2 = "fresh"
	display_date = str(day) if day > 9 else '0' + str(day)
	lastModifiedTime = date_template.format(display_date) + "09:00"

	output = etree.Element('entries')
	output.append(create_elem('title', rand_title))
	output.append(create_elem('tagged', tag1))
	output.append(create_elem('tagged', tag2))
	output.append(create_elem('isMarked', is_marked))
	output.append(create_elem('lastModified', lastModifiedTime))
	output.append(create_elem('recursion', '0'))

	return output

def create_event(day, is_marked):
	rand_title = "Work on " + random.choice(modules)
	display_date = str(day) if day > 9 else '0' + str(day)
	start = date_template.format(display_date) + "18:00"
	end = date_template.format(display_date) + "22:00"
	tag = "pain"
	lastModifiedTime = date_template.format(display_date) + "09:00"

	rand = random.random()

	output = etree.Element('entries')
	output.append(create_elem('title', rand_title))
	output.append(create_elem('start', start))
	output.append(create_elem('end', end))
	output.append(create_elem('tagged', tag))
	output.append(create_elem('isMarked', is_marked))
	output.append(create_elem('lastModified', lastModifiedTime))
	output.append(create_elem('recursion', '0'))

	return output

root = etree.Element('taskmanager')

for i in range(start_day, end_day):
	is_marked = 'true' if i < yesterday else 'false'
	root.append(create_task(i, is_marked))
	root.append(create_floating_task(i, is_marked))
	root.append(create_event(i, is_marked))


s = etree.tostring(root, xml_declaration=True, encoding='UTF-8', standalone='yes')
with open(output_file, 'w') as f:
	f.write(prettify(s))



