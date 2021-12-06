# Prank email with SMTP - API 2021  HEIG - Tobie Praz, Damiano Mondaini

# Description du projet

Remarque importante: Ce projet est à titre éducatif et ne doit pas être utilisé à d'autres fins.
Ce repository GitHub permet d'automatiser l'envoi de mails forgés via un serveur SMTP et une liste de victimes configurables.
Pour chaque exécution du programme une série de mails sera envoyée selon le ficher de configuration `config.json`.

Instructions for setting up a mock SMTP server (with Docker - which you will learn all about in the next 2 weeks). The user who wants to experiment with your tool but does not really want to send pranks immediately should be able to use a mock SMTP server. For people who are not familiar with this concept, explain it to them in simple terms. Explain which mock server you have used and how you have set it up.

Clear and simple instructions for configuring your tool and running a prank campaign. If you do a good job, an external user should be able to clone your repo, edit a couple of files and send a batch of e-mails in less than 10 minutes.

A description of your implementation: document the key aspects of your code. It is probably a good idea to start with a class diagram. Decide which classes you want to show (focus on the important ones) and describe their responsibilities in text. It is also certainly a good idea to include examples of dialogues between your client and an SMTP server (maybe you also want to include some screenshots here).