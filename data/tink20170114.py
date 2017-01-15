__author__ = 'rcj1492'
__created__ = '2017.01'
__license__ = 'MIT'

# https://stackoverflow.com/questions/4913349/haversine-formula-in-python-bearing-and-distance-between-two-gps-points#4913653

from math import radians, cos, sin, asin, sqrt, atan2, degrees

def haversine_distance(lon1, lat1, lon2, lat2):
    """
    Calculate the great circle distance between two points
    on the earth (specified in kilometers)
    """
    # convert decimal degrees to radians
    lon1, lat1, lon2, lat2 = map(radians, [lon1, lat1, lon2, lat2])

    # haversine formula
    dlon = lon2 - lon1
    dlat = lat2 - lat1
    a = sin(dlat/2)**2 + cos(lat1) * cos(lat2) * sin(dlon/2)**2
    c = 2 * asin(sqrt(a))
    r = 6371 # Radius of earth in kilometers. Use 3956 for miles
    return c * r

def haversine_bearing(lon1, lat1, lon2, lat2):
    """
    Calculate the compass bearing between two points
    on the earth (specified in decimal degrees)
    """
    # convert decimal degrees to radians
    lon1, lat1, lon2, lat2 = map(radians, [lon1, lat1, lon2, lat2])

    # apply arctangent function
    bearing = atan2(sin(lon2 - lon1) * cos(lat2), cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(lon2 - lon1))
    bearing = degrees(bearing)
    # correct for negative degrees
    return (bearing + 360) % 360

raw_path = 'moves-raw.json'
trans_path = 'protest-array-trans.json'
vector_single = 'vector-single.json'
protest_path = 'protest-boot.json'
vector_path = 'vector-boot.json'

from time import time
from pprint import pprint
from labpack.records.settings import save_settings, load_settings
from labpack.records.id import labID

trans_details = load_settings(trans_path)['protests']
protest_details = load_settings(protest_path)['protests']
bootstrap_list = []
for protest in trans_details:
    for vector in protest['vectors']:
        bootstrap_list.append(vector)

pprint(bootstrap_list)
save_settings(vector_single, {'vectors': bootstrap_list}, overwrite=True)
# bootstrap_list = []
# protest_map = {
#     'LfqaDlRpblNaDQ40CNc4lXVU_551Nau62y86': {
#         'pid': 'LfqaDlRpblNaDQ40CNc4lXVU_551Nau62y86',
#         'size': 200,
#         'name': 'Not My President',
#         'creator_name': 'Citizens of NYC',
#         'participants': []
#     },
#     'MX7vOpl0ZlPtLggibnZFXlRHO2WSzTDWLf1V': {
#         'pid': 'MX7vOpl0ZlPtLggibnZFXlRHO2WSzTDWLf1V',
#         'size': 45,
#         'name': 'Blood On Your Hands',
#         'creator_name': 'People Against Animal Abuse',
#         'participants': []
#     },
#     'nn0I8LCb7OMemg8sgqUVkA5iZqQq2y3QcXsb': {
#         'pid': 'nn0I8LCb7OMemg8sgqUVkA5iZqQq2y3QcXsb',
#         'size': 100,
#         'name': 'Black Lives Matter',
#         'creator_name': 'Black Lives Matter',
#         'participants': []
#     },
#     'rzG6MqSN58TDQ-zY6Bf6Geg2SGgsbn0TKhMl': {
#         'pid': 'rzG6MqSN58TDQ-zY6Bf6Geg2SGgsbn0TKhMl',
#         'size': 24,
#         'name': 'Strike Casper',
#         'creator_name': 'Creative Teamsters 161',
#         'participants': []
#     }
# }
# for i in range(len(trans_details['protests'])):
#     protest = trans_details['protests'][i]
#     pid = protest['pid']
#     if pid in protest_map.keys():
#         protest_map[pid]['creator_id'] = labID().id36
#         protest_map[pid]['participants'].append(protest_map[pid]['creator_id'])
#         protest_map[pid]['participants'].append(protest['vectors'][0]['uid'])
#         for j in range(protest_map[pid]['size']):
#             protest_map[pid]['participants'].append(labID().id36)
#     bootstrap_list.append(protest_map[pid])
# pprint(bootstrap_list)
# save_settings(protest_path, {'protests': bootstrap_list}, overwrite=True)

# raw_details = load_settings(raw_path)
# # pprint(raw_details)
# from labpack.records.time import labDT
# from labpack.records.id import labID
# modified_path = 'protest-array-trans.json'
# movement_history = []
# for key, value in raw_details.items():
#     pid = labID().id36
#     uid = labID().id36
#     protest_details = {
#         'name': key,
#         'pid': pid,
#         'vectors': []
#     }
#     for i in range(len(value)):
#         ts2 = labDT.fromISO(value[i]['time']).epoch()
#         movement_details = {
#             'pid': pid,
#             'uid': uid,
#             'ts': ts2,
#             'lat': value[i]['lat'],
#             'lon': value[i]['lon']
#         }
#         if i != 0:
#             ts1 = labDT.fromISO(value[i - 1]['time']).epoch()
#             haversine_kwargs = {
#                 'lat2': value[i]['lat'],
#                 'lon2': value[i]['lon'],
#                 'lat1': value[i - 1]['lat'],
#                 'lon1': value[i - 1]['lon'],
#             }
#             km_dist = haversine_distance(**haversine_kwargs)
#             if ts2 - ts1 > 0:
#                 movement_details['speed'] = km_dist * 1000 / (ts2 - ts1)
#                 movement_details['degree'] = int(haversine_bearing(**haversine_kwargs))
#                 protest_details['vectors'].append(movement_details)
#     movement_history.append(protest_details)
# pprint(movement_history)
# save_settings(modified_path, { 'protests': movement_history }, overwrite=True)
